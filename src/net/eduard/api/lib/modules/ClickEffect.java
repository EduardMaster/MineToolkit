package net.eduard.api.lib.modules;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickEffect {
	public void onClick(InventoryClickEvent event, int page);
}
