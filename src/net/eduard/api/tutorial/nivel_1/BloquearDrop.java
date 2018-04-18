package net.eduard.api.tutorial.nivel_1;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
/**
 * Não poder dropar itens
 * @author Eduard
 *
 */
public class BloquearDrop implements Listener{
	
	@EventHandler
	public void aoJogadorDroparItem(PlayerDropItemEvent e) {

		e.setCancelled(true);
	}

}
