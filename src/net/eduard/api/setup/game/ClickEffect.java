package net.eduard.api.setup.game;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ClickEffect {
	public void onClick(InventoryClickEvent event, int page);
}
