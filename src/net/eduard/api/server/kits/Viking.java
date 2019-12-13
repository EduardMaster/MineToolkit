package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.eduard.api.lib.Mine;
import net.eduard.api.server.kit.KitAbility;

public class Viking extends KitAbility {
	public double damage = 2;

	public Viking() {
		setIcon(Material.IRON_AXE, "�fBatalhe melhor usando Machados");
	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (Mine.isUsing(p, "_AXE")) {
					e.setDamage(e.getDamage() + damage);
				}
			}

		}
	}
}
