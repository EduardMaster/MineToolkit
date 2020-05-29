package net.eduard.api.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import net.eduard.api.lib.game.Tag;

public  class TagUpdateEvent extends PlayerEvent implements Cancellable
	 {
	
	 private Tag tag;
	 private boolean cancelled;
	
	 public boolean isCancelled() {
	 return cancelled;
	 }
	
	 public void setCancelled(boolean cancelled) {
	 this.cancelled = cancelled;
	 }
	
	 @Override
	 public HandlerList getHandlers() {
	 return handlers;
	 }
	
	 public static HandlerList getHandlerList() {
	 return handlers;
	 }
	
	 private static final HandlerList handlers = new HandlerList();
	
	 public TagUpdateEvent(Tag tag, Player who) {
	 super(who);
	 setTag(tag);
	 }
	
	 public Tag getTag() {
	 return tag;
	 }
	
	 public void setTag(Tag tag) {
	 this.tag = tag;
	 }
	 }