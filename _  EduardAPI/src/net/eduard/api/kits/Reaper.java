package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.game.Ability;

public class Reaper extends Ability {
	public Material reaper = Material.WOOD_HOE;

	public Reaper() {
		setIcon(Material.WOOD_HOE, "§fEnvene seus inimigos com Wither");
		getPotions().add(new PotionEffect(PotionEffectType.WITHER, 3, 20 * 5));
	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (p.getItemInHand() == null)
					return;
				if (p.getItemInHand().getType() == reaper)
					if (e.getEntity() instanceof LivingEntity) {
						LivingEntity livingEntity = (LivingEntity) e
								.getEntity();
						givePotions(livingEntity);
					}
			}

		}
	}
}
