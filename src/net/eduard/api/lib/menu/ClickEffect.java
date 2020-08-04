package net.eduard.api.lib.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

/**
 * Efeito quando clicar
 *
 * @author Eduard
 *
 */
public interface ClickEffect extends Consumer<InventoryClickEvent> {

	/**
	 * @deprecated Use lambda em vez
	 * @param event Evento de Clique no Inventario
	 * @param page Pagina clicada no Menu
	 */
	@Deprecated
	 default void onClick(InventoryClickEvent event, int page){
		accept(event);
	}

}
