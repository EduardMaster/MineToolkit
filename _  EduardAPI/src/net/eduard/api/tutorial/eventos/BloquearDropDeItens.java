package net.eduard.api.tutorial.eventos;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

import net.eduard.api.manager.TimeManager;

public class BloquearDropDeItens extends TimeManager{
	
	@EventHandler
	public void JogadorNaoDropar(PlayerDropItemEvent e) {

		e.setCancelled(true);
	}

}
