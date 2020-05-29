package net.eduard.api.lib.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import net.eduard.api.lib.game.DisplayBoard;

public class ScoreUpdateEvent extends PlayerEvent implements Cancellable {
	private DisplayBoard score;
	private boolean cancelled;

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private static final HandlerList handlers = new HandlerList();

	public ScoreUpdateEvent(Player who, DisplayBoard score) {
		super(who);
		setScore(score);
	}

	public DisplayBoard getScore() {
		return score;
	}

	public void setScore(DisplayBoard score) {
		this.score = score;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}

