package net.eduard.api.lib.click;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerClickEntityEffect {
	
	void onClickAtEntity(Player player, Entity entity, ItemStack item);

}