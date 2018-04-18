package net.eduard.api.util.hologram;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

public interface TouchHandler {

	public void onTouch(@Nonnull Hologram hologram, @Nonnull Player player, @Nonnull TouchAction action);

}
