package net.eduard.api.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;

public class LumberJackMega extends Ability{
	
	public LumberJackMega() {
		setIcon(Material.DIAMOND_AXE, "§fDestroi uma arvore gigante de uma so vez");
		add(Material.WOOD_AXE);
		setTime(10);
	}

	public void check(Location loc) {
		int range = 3;
		int  high = 40;
		int X = loc.getBlock().getX();
		int Y = loc.getBlock().getY();
		int Z = loc.getBlock().getZ();
		for (int x = X-range;x<=X+range;x++) {
			for (int z = Z-range;z<=Z+range;z++) {
				for (int y = Y;y<=Y+high;y++) {
					loc = new Location(loc.getWorld(), x, y, z);
					Material type = loc.getBlock().getType();
					if (type.name().contains("LOG")) {
						loc.getBlock().breakNaturally();
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void event(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (Mine.isUsing(p, "_AXE")) {
				if (e.getBlock().getType().name().contains("LOG")) {
					check(e.getBlock().getLocation());
				}
				
			}

		}

	}

}
