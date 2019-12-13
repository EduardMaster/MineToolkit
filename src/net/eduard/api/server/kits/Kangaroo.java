package net.eduard.api.server.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.lib.game.Jump;
import net.eduard.api.server.kit.KitAbility;

public class Kangaroo extends KitAbility {

	public static ArrayList<Player> inEffect = new ArrayList<>();
	public Jump front = new Jump( true, 2, 0.5,null);
	public Jump high = new Jump(false, 1, 1.2,null);
	public int maxDamage = 3;
	public Kangaroo() {
		setIcon(Material.FIREWORK, "�fSe mova mais rapido");
		add(Material.FIREWORK);
		setClick(new PlayerClick(Material.FIREWORK,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (!onCooldown(player)) {
						if (!inEffect.contains(player)) {
							if (Mine.isFlying(player)) {
								inEffect.add(player);
							}
							if (player.isSneaking()) {
								front.create(player);
							} else {
								high.create(player);
							}
						}

					}
				}
			}
		}));
		setActiveCooldownOnPvP(true);
		setTime(5);
	}

	@EventHandler
	public void event(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.FALL) {
				if (hasKit(p)) {
					if (e.getDamage() > maxDamage) {
						e.setDamage(maxDamage);
					}
				}
			}
		}

	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (Mine.isOnGround(p)) {
			inEffect.remove(p);
		}
	}

}
