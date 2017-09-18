package net.eduard.api.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import net.eduard.api.game.Ability;
import net.eduard.api.setup.ItemAPI;

public class LumberJack extends Ability {
	public LumberJack() {
		setIcon(Material.WOOD_AXE, "§fQuebre uma arvore de uma só vez");
		add(Material.WOOD_AXE);
	}

	public boolean check(Location loc) {
		Material type = loc.getBlock().getType();
		if (type.name().contains("LOG")) {
			loc.getBlock().breakNaturally();
			return true;
		}
		return false;
	}

	@EventHandler
	public void event(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (ItemAPI.isUsing(p, "_AXE")) {
				if (e.getBlock().getType().name().contains("LOG")) {
					Location loc;
					while (check(loc = e.getBlock().getLocation().add(0, 1, 0))) {
						loc.add(0, 1, 0);
					}
				}
			}

		}

	}

}
