package net.eduard.api.setup.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DamagerManager implements Listener {
	public static Entity getLastDamager(Entity entity) {
		Entity damager = lastPvP.get(entity);
		if (damager != null) {
			if (damager instanceof Projectile) {
				Projectile projectile = (Projectile) damager;

				if (projectile.getShooter() != null && projectile instanceof Entity) {
					return (Entity) projectile.getShooter();
				}

			}
		}
		return damager;
	}

	private static Map<Entity, Entity> lastPvP = new HashMap<>();
	private static DamagerManager classe = new DamagerManager();
	static {
		Bukkit.getPluginManager().registerEvents(classe, JavaPlugin.getProvidingPlugin(DamagerManager.class));
	}

	@EventHandler
	public void aoIrPvP(EntityDamageByEntityEvent e) {
		lastPvP.put(e.getEntity(), e.getDamager());
	}

}
