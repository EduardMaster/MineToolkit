package net.eduard.api.tutorial.eventos;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventoCustomizado extends Event implements Listener,Cancellable{
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		EventoCustomizado event = new EventoCustomizado("Mensagem a ser Enviada");
		Bukkit.getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			p.sendMessage(event.getMessage());
		}
	}
	  private static final HandlerList handlers = new HandlerList();
	    private String message;
	    private boolean cancelled;

	    public EventoCustomizado(String example) {
	        message = example;
	    }

	    public String getMessage() {
	        return message;
	    }

	    @Override
		public boolean isCancelled() {
	        return cancelled;
	    }

	    @Override
		public void setCancelled(boolean cancel) {
	        cancelled = cancel;
	    }

	    @Override
		public HandlerList getHandlers() {
	        return handlers;
	    }

	    public static HandlerList getHandlerList() {
	        return handlers;
	    }
}
