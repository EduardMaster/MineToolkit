package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.kit.KitAbility;

public class Snail extends KitAbility {
	public double chance = 0.35;

	public Snail() {
		setIcon(Material.SOUL_SAND, "�fDeixe seus inimigos lentos");
		getPotions().add(new PotionEffect(PotionEffectType.SLOW, 1, 20 * 5));
	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (e.getEntity() instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) e.getEntity();
					if (Mine.getChance(chance)) {
						for (PotionEffect pot : getPotions()) {
							pot.apply(livingEntity);
						}
					}
				}
			}

		}
	}
}
