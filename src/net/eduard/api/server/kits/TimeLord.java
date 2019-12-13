package net.eduard.api.server.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;


import net.eduard.api.lib.click.PlayerClickEntity;
import net.eduard.api.lib.click.PlayerClickEntityEffect;
import net.eduard.api.server.kit.KitAbility;

public class TimeLord extends KitAbility {

	public static ArrayList<Player> inEffect = new ArrayList<>();

	public int effectSeconds = 10;

	public TimeLord() {
		setIcon(Material.WATCH, "�fParalize seus inimigos");
		add(Material.WATCH);
		setClick(new PlayerClickEntity(Material.WATCH,new PlayerClickEntityEffect() {
			
			@Override
			public void onClickAtEntity(Player player, Entity entity, ItemStack item) {
				if (hasKit(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						if (cooldown(player)) {
							inEffect.add(target);
							asyncDelay(new Runnable() {

								@Override
								public void run() {
									inEffect.remove(target);
								}
							},20*2);
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
