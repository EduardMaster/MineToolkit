package net.eduard.api.lib.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface PagedMenu {

	public Inventory open(Player player, int page);

	public default Inventory open(Player player) {
		return open(player, 1);
	}
	public int getPageOpen(Player player);
	public boolean isOpen(Player player);
	

}
