package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.modules.KitType;
import net.eduard.api.server.kit.KitAbility;

public class Milk extends KitAbility {

	public Milk() {
		super("Milk", KitType.MILK);
		setIcon(Material.MILK_BUCKET, "�fTome seu leitinho");
		getPotions().add(new PotionEffect(PotionEffectType.REGENERATION, 0, 20 * 5));
		getPotions().add(new PotionEffect(PotionEffectType.SPEED, 1, 20 * 5));
	}

	@EventHandler
	public void consome(PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();

		if (hasKit(p)) {
			if (e.getItem().getType() == Material.MILK_BUCKET) {
				if (cooldown(p)) {
					for (PotionEffect pot : getPotions()) {
						pot.apply(p);
					}
				}
			}
		}
	}

}
