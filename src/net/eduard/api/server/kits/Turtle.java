package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.kit.KitAbility;

public class Turtle extends KitAbility {
	public double chance = 0.3;
	public double damage = 1;

	public Turtle() {
		setIcon(Material.LEATHER_CHESTPLATE, "�fAo estiver agaixado vai ficar quase invuneravel");
		setMessage("�6Voce n�o pode bater enquanto estiver agaixando");
		getPotions().add(new PotionEffect(PotionEffectType.POISON, 0, 20 * 5));
	}

	
	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if (hasKit(p)) {
				if (p.isSneaking()) {
					e.setCancelled(true);
					p.sendMessage(getMessage());;
				}
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

	@EventHandler
	public void event(EntityDamageEvent e) {;
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (hasKit(p)) {
				if (p.isSneaking()) {
					e.setDamage(damage);
				}
			}
		}
	}
}
