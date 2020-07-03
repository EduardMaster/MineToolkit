package net.eduard.api.lib.game;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * Efeito quando clicar
 * 
 * @author Eduard
 *
 */
public interface ClickEffect extends Consumer<InventoryClickEvent> {
	@Deprecated
	 default void onClick(InventoryClickEvent event, int page){
		accept(event);
	}

}
