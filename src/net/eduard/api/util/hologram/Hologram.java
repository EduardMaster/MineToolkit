package net.eduard.api.util.hologram;

import java.util.Collection;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract interface Hologram {
	boolean isSpawned();
	void spawn(@Nonnull @Nonnegative long ticks);
	boolean spawn();
	boolean despawn();
	void setText(@Nullable String text);
	String getText();
	void update();
	void update(long interval);
	void setLocation(@Nonnull Location loc);
	Location getLocation();
	void move(@Nonnull Location loc);
	void setTouchable(boolean flag);
	boolean isTouchable();
	void addTouchHandler(@Nonnull TouchHandler handler);
	void removeTouchHandler(@Nonnull TouchHandler handler);
	Collection<TouchHandler> getTouchHandlers();
	void clearTouchHandlers();
	void addViewHandler(@Nonnull ViewHandler handler);
	void removeViewHandler(@Nonnull ViewHandler handler);
	@Nonnull
	Collection<ViewHandler> getViewHandlers();
	void clearViewHandlers();
	@Nonnull
	Hologram addLineBelow(String text);
	@Nullable
	Hologram getLineBelow();
	boolean removeLineBelow();
	@Nonnull
	Collection<Hologram> getLinesBelow();
	@Nonnull
	Hologram addLineAbove(String text);
	@Nullable
	Hologram getLineAbove();
	boolean removeLineAbove();
	@Nonnull
	Collection<Hologram> getLinesAbove();
	@Nonnull
	Collection<Hologram> getLines();
	void setAttachedTo(@Nullable Entity entity);
	@Nullable
	Entity getAttachedTo();
}
