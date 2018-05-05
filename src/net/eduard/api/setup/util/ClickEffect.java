package net.eduard.api.setup.util;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickEffect {
	public void onClick(InventoryClickEvent event, int page);
}
