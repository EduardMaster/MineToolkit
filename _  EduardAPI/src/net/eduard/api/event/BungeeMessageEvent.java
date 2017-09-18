package net.eduard.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.google.common.io.ByteArrayDataInput;

public class BungeeMessageEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	public BungeeMessageEvent(Player player, ByteArrayDataInput data) {
		this.player = player;
		this.data = data;
		this.request = data.readUTF();
	}
	public ByteArrayDataInput getData() {
		return data;
	}
	public Player getPlayer() {
		return player;
	}
	public String getRequest() {
		return request;
	}
	private ByteArrayDataInput data;
	private Player player;
	private String request;
	
}
