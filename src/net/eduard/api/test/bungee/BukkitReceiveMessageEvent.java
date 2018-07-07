package net.eduard.api.test.bungee;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class BukkitReceiveMessageEvent extends Event implements Cancellable{

	private boolean cancelled;
	
	private Player player;
	
	private String[] args;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String... args) {
		this.args = args;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	private static final HandlerList HANDLER = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return HANDLER;
	}
}
