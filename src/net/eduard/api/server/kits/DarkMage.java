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

public class DarkMage extends KitAbility {
	public double chance = 0.3;

	public DarkMage() {
		setIcon(Material.COAL, "�fSegue seus inimigos");
		getPotions().add(new PotionEffect(PotionEffectType.BLINDNESS, 0, 20 * 5));
	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (p.getItemInHand() == null)
					return;
				if (Mine.getChance(chance))
					if (e.getEntity() instanceof LivingEntity) {
						LivingEntity livingEntity = (LivingEntity) e.getEntity();
						for (PotionEffect pot : getPotions()) {
							pot.apply(livingEntity);
						}

					}
				}

		}
	}
}
