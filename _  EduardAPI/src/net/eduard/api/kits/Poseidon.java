package net.eduard.api.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.game.Ability;

public class Poseidon extends Ability {

	public Poseidon() {
		setIcon(Material.WATER_BUCKET, "§fFique mais forte na agua");
		getPotions().add(new PotionEffect(PotionEffectType.SPEED, 1, 20 * 5));
		getPotions().add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, 20 * 5));
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				if (hasKit(p)) {
					Material type = e.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
					if (type == Material.WATER | type == Material.STATIONARY_WATER) {
						givePotions(p);
					}
				}
			}
		});
		
	}

}
