package net.eduard.api.lib.click;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerClickEffect {
	
	void onClick(Player player, Block block, ItemStack item);

}