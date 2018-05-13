package net.eduard.api.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;

public class Ninja extends Ability {

	public static HashMap<Player, Player> targets = new HashMap<>();


	public Ninja() {
		setIcon(Material.COAL_BLOCK, "§fTeleporte ate seus inimigos");
		setTime(10);
	}


	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (e.getEntity() instanceof Player) {
					Player target = (Player) e.getEntity();
					targets.put(p, target);
				}
			}

		}
	}
	@EventHandler
	public void event(PlayerToggleSneakEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (e.isSneaking()) {
				if (targets.containsKey(p)) {
					if (cooldown(p)) {
						Player target = targets.get(p);
						Mine.teleport(p, target.getLocation());
						e.setCancelled(true);
					}
				}
			
			}
		}
		
	}
	

}
