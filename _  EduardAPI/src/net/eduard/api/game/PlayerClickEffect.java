package net.eduard.api.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface PlayerClickEffect {
	
	public abstract void onClick(Player player,Block block,ItemStack item);

}
