package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.eduard.api.game.Ability;
import net.eduard.api.setup.ItemAPI;

public class Achilles extends Ability {

	public double damageReduction = 2;
	public double extraDamage = 4;
	public double minDamage = 1;
	public Achilles() {
		setIcon(Material.WOOD_SWORD, "§fGanhe força baseada na força do Inimigo");
	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (hasKit(p)) {
				if (e.getDamager() instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) e.getDamager();
					if (ItemAPI.isUsing(livingEntity, "WOOD")) {
						e.setDamage(e.getDamage() + extraDamage);
					} else {
						double red = e.getDamage() - damageReduction;
						double calc = red <= minDamage ? minDamage : red;
						e.setDamage(calc);
					}
				}
			}

		}
	}
}
