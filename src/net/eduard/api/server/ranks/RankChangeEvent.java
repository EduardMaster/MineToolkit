package net.eduard.api.server.ranks;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class RankChangeEvent extends PlayerEvent implements Cancellable{
	private boolean cancelled;
	private Rank previousRank;
	private Rank nextRank;
	public RankChangeEvent(Player who) {
		super(who);
	}
	

	@Override
	public HandlerList getHandlers() {
		return HANDLER;
	}
	public Rank getPreviousRank() {
		return previousRank;
	}


	public void setPreviousRank(Rank previousRank) {
		this.previousRank = previousRank;
	}
	public Rank getNextRank() {
		return nextRank;
	}


	public void setNextRank(Rank nextRank) {
		this.nextRank = nextRank;
	}
	public boolean isCancelled() {
		return cancelled;
	}


	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	private static final HandlerList HANDLER = new HandlerList();

}
