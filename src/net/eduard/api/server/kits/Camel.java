package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.server.kit.KitAbility;

public class Camel extends KitAbility {
	public Camel() {
		setIcon(Material.SAND, "�fFique mais forte na areia");
		getPotions().add(new PotionEffect(PotionEffectType.REGENERATION, 0, 20 * 5));
		getPotions().add(new PotionEffect(PotionEffectType.SPEED, 1, 20 * 5));
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			Material type = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
			if (type == Material.SAND | type == Material.SANDSTONE) {
				for (PotionEffect pot : getPotions()) {
					pot.apply(p);
				}
			}	
		}
	}

}
