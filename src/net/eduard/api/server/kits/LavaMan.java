package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.server.kit.KitAbility;

public class LavaMan extends KitAbility {

	public LavaMan() {
		setIcon(Material.WATER_BUCKET, "�fFique mais forte na agua");
		getPotions().add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 20 * 5));
		getPotions().add(new PotionEffect(PotionEffectType.SPEED, 0, 20 * 5));
		getPotions().add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 0, 20 * 5));
		
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			Material type = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
			if (type == Material.LAVA | type == Material.STATIONARY_LAVA) {
				for (PotionEffect pot : getPotions()) {
					pot.apply(p);
				}
			}
		}
	}

}
