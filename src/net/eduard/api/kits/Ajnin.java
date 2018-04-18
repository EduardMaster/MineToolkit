package net.eduard.api.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Ability;

public class Ajnin extends Ability {

	public static HashMap<Player, Player> targets = new HashMap<>();

	public int maxDistance = 50;

	public Ajnin() {
		setIcon(Material.NETHER_STAR, "§fTeleporte seus inimigos ate você");
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
					Player target = targets.get(p);
					if (target != null) {
						if (target.getLocation().distance(p.getLocation()) <= maxDistance) {
							if (cooldown(p)) {
								Mine.teleport(target, p.getLocation());
								e.setCancelled(true);

							}
						}
					}

				}

			}
		}

	}

}
