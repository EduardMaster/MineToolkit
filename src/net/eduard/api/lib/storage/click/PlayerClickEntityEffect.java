package net.eduard.api.lib.storage.click;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerClickEntityEffect {
	
	public abstract void onClickAtEntity(Player player,Entity entity,ItemStack item);

}