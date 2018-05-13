package net.eduard.api.kits;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.Mine.LocationEffect;
import net.eduard.api.lib.storage.click.PlayerClickEntity;
import net.eduard.api.lib.storage.click.PlayerClickEntityEffect;
import net.eduard.api.lib.storage.game.Ability;

public class Infernor extends Ability {
	
	public static HashMap<Player, Location> locations = new HashMap<>();
	public static HashMap<Player, Player> targets = new HashMap<>();
	public static HashMap<Player, List<Location>> arenas = new HashMap<>();
	public int high = 100;
	public int size = 10;
	public Material mat = Material.NETHERRACK;
	public int data = 0;
	public double chance = 0.33;
	public int effectSeconds = 5;
	public Infernor() {
		setIcon(Material.NETHER_STAR, "§fChame seus inimigos para um Duelo 1v1");
		add(Material.NETHER_STAR);
		message("§6Voce esta invuneravel por 5 segundos!");
		setPrice(60 * 1000);
		setClick(new PlayerClickEntity(Material.NETHER_STAR,new PlayerClickEntityEffect() {
			
			@Override
			public void onClickAtEntity(Player player, Entity entity, ItemStack item) {
				if (entity instanceof Player) {
					Player target = (Player)entity;
					
					if (onCooldown(player)){
						return;
					}
					
					if (arenas.containsKey(target) | arenas.containsKey(player)) {
						player.sendMessage("§6Voce ja esta em Batalha!");
						return;
					}
					
					
					Location loc = player.getLocation().add(0, high, 0);
					List<Location> arena = createArena(player);
					locations.put(player, player.getLocation());
					locations.put(target, target.getLocation());
					targets.put(player, target);
					targets.put(target, player);
					arenas.put(player, arena);
					arenas.put(target, arena);
					Mine.makeInvunerable(player, effectSeconds);
					Mine.makeInvunerable(target, effectSeconds);
					player.teleport(loc.clone().add(size - 2, 1, 2 - size)
							.setDirection(player.getLocation().getDirection()));
					target.teleport(loc.clone().add(2 - size, 1, size - 2)
							.setDirection(target.getLocation().getDirection()));
					sendMessage(player);
					sendMessage(target);

			}

			}
		}));
		
	}
	public List<Location> createArena(Player p){
		Location x = p.getLocation().add(0, 100, 0);
		List<Location> locs = Mine.getBox(x, size, size, size, new LocationEffect() {
			
			@SuppressWarnings("deprecation")
			@Override
			public boolean effect(Location location) {
				location.getBlock().setType(mat);
				location.getBlock().setData((byte) data);
//				location.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
				return true;
			}
		});
		Mine.getBox(p.getLocation().add(0, 100, 0), size-1, size-1, size-1, new LocationEffect() {
			
			@Override
			public boolean effect(Location location) {
				location.getBlock().setType(Material.AIR);
				if (Mine.getChance(chance)){
					location.getBlock().setType(Material.FIRE);
				}
				
				return false;
			}
		});
		return locs;
	}

	@EventHandler
	public void event(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (arenas.containsKey(p)) {
			if (e.getBlock().getType() == mat) {
				e.setCancelled(true);
			}
		}

	}

	@EventHandler
	public void event(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (arenas.containsKey(p)) {
			win(targets.get(p), p);
		}
	}

	@EventHandler
	public void event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (arenas.containsKey(p)) {
			win(targets.get(p), p);
		}
	}


	public void win(Player winner, Player loser) {
		winner.sendMessage("§6Voce venceu a batalha!");

		List<Location> arena = arenas.get(winner);
		for (Location location : arena) {
			location.getBlock().setType(Material.AIR);
		}
		loser.sendMessage("§6Voce perdeu a batalha!");
		winner.teleport(locations.get(winner)
				.setDirection(winner.getLocation().getDirection()));
		loser.teleport(locations.get(loser)
				.setDirection(loser.getLocation().getDirection()));
		targets.remove(winner);
		targets.remove(loser);
		arenas.remove(winner);
		arenas.remove(loser);
		locations.remove(winner);
		locations.remove(loser);
	}
}
