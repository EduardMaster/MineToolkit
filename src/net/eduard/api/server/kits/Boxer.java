package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.eduard.api.lib.Mine;
import net.eduard.api.server.kit.KitAbility;

public class Boxer extends KitAbility {
	public double damageReduction = 1;
	public double damage = 4;

	public Boxer() {
		setIcon(Material.STONE_SWORD, "�fReduza 1 de dano recebido",
				"�fCause 4 de dano usando Nada");
	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (Mine.isUsing(p, Material.AIR)) {
					e.setDamage(damage);
				}
			}
		}
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (hasKit(p)) {
				e.setDamage(e.getDamage() - damageReduction);
			}
		}
	}
}
