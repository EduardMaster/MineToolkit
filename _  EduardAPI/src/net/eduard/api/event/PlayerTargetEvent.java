package net.eduard.api.event;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerTargetEvent extends PlayerEvent {

	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	private static final HandlerList handlers = new HandlerList();
	private LivingEntity entity;
	public PlayerTargetEvent(Player player, LivingEntity livingEntity) {
		super(player);
		setEntity(livingEntity);

	}

	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

}
