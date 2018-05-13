package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.eduard.api.lib.storage.game.Ability;
import net.eduard.api.lib.storage.game.Explosion;

import org.bukkit.event.entity.PlayerDeathEvent;

public class Tank extends Ability {

	public Tank() {
		setIcon(Material.TNT, "§fSeja um Terrista");
		explosion(new Explosion(6, false, false));
	}

	@EventHandler
	public void event(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (hasKit(p)) {
				if (e.getCause() == DamageCause.BLOCK_EXPLOSION | e.getCause() == DamageCause.ENTITY_EXPLOSION) {
					e.setCancelled(true);
				}
			}

		}
	}

	@EventHandler
	public void event(PlayerDeathEvent e) {
		if (e.getEntity().getKiller() != null) {
			Player p = e.getEntity().getPlayer();
			if (hasKit(p)) {
				makeExplosion(p);
			}

		}
		if (e.getEntity() instanceof Player) {
			Player p = e.getEntity();
			if (hasKit(p)) {
				makeExplosion(p);
			}
		}

	}

}
