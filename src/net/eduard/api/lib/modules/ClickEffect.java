package net.eduard.api.lib.modules;

import org.bukkit.event.inventory.InventoryClickEvent;
/**
 * Efeito quando clicar
 * @author Eduard
 *
 */
public interface ClickEffect {
	public void onClick(InventoryClickEvent event, int page);
}
