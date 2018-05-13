package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;

public class Turtle extends Ability {
	public double chance = 0.3;
	public double damage = 1;

	public Turtle() {
		setIcon(Material.LEATHER_CHESTPLATE, "§fAo estiver agaixado vai ficar quase invuneravel");
		message("§6Voce não pode bater enquanto estiver agaixando");
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
						givePotions(livingEntity);
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
