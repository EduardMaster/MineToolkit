package net.eduard.api.server.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.server.kit.KitAbility;

public class Poseidon extends KitAbility {

	public Poseidon() {
		setIcon(Material.WATER_BUCKET, "�fFique mais forte na agua");
		getPotions().add(new PotionEffect(PotionEffectType.SPEED, 1, 20 * 5));
		getPotions().add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 20 * 5));
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(getPluginInstance(), new Runnable() {
			
			@Override
			public void run() {
				if (hasKit(p)) {
					Material type = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
					if (type == Material.WATER | type == Material.STATIONARY_WATER) {
						for (PotionEffect pot : getPotions()) {
							pot.apply(p);
						}
					}
				}
			}
		});
		
	}

}
