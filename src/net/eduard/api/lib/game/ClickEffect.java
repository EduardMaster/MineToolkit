package net.eduard.api.lib.game;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Efeito quando clicar
 * 
 * @author Eduard
 *
 */
public interface ClickEffect {
	public void onClick(InventoryClickEvent event, int page);

	public default void onClick(Player player, int page, int slot, ItemStack item) {

	}
}
