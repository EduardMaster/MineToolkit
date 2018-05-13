package net.eduard.api.kits;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;
import net.eduard.api.lib.storage.game.Explosion;

public class DemoMan extends Ability {
	
	public static ArrayList<Location> inEffect = new ArrayList<>();

	public DemoMan() {
		setIcon(Material.GRAVEL, "§fExploda seus inimigos com armadilhas");
		add(new ItemStack(Material.GRAVEL, 8));
		explosion(new Explosion(6, false, false));

	}

	@EventHandler
	public void event(BlockBreakEvent e) {
		if (e.getBlock().getType() == Material.GRAVEL) {
			inEffect.remove(e.getBlock().getLocation());
		}
	}

	@EventHandler
	public void event(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (e.getBlock().getType() == Material.GRAVEL) {
				inEffect.add(e.getBlock().getLocation());
			}
		}
	}

	@EventHandler
	public void event(PlayerMoveEvent e) {
		if (!Mine.equals(e.getFrom(), e.getTo())) {
			for (Location loc : inEffect) {
				if (Mine.equals(loc, e.getTo().subtract(0, 1, 0))) {
					inEffect.remove(loc);
					getExplosion().create(loc);
					break;
				}
			}
		}
	}

}
