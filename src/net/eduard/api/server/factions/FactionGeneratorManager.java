package net.eduard.api.server.factions;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface FactionGeneratorManager {
	
	public ItemStack getGenerator(EntityType type);
	
	public EntityType getGeneratorType(ItemStack item);
	
	public boolean isGenerator(ItemStack item);
}
