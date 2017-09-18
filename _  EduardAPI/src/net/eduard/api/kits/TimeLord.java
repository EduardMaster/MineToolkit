package net.eduard.api.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.API;
import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClickEntity;
import net.eduard.api.game.PlayerClickEntityEffect;

public class TimeLord extends Ability {

	public static ArrayList<Player> inEffect = new ArrayList<>();

	public int effectSeconds = 10;

	public TimeLord() {
		setIcon(Material.WATCH, "§fParalize seus inimigos");
		add(Material.WATCH);
		setClick(new PlayerClickEntity(Material.WATCH,new PlayerClickEntityEffect() {
			
			@Override
			public void onClickAtEntity(Player player, Entity entity, ItemStack item) {
				if (hasKit(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						if (cooldown(player)) {
							inEffect.add(target);
							API.TIME.delay(2,new Runnable() {

								@Override
								public void run() {
									inEffect.remove(target);
								}
							});
						}
					}

				}
			}
		}));
		setTime(30);
	

	}

	@EventHandler
	public void event(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			inEffect.remove(p);

		}
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (inEffect.contains(p)) {
			p.teleport(e.getFrom());
		}
	}
}
