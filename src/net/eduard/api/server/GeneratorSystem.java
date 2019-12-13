package net.eduard.api.server;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public interface GeneratorSystem {
	
	public ItemStack getGenerator(EntityType type);
	
	public EntityType getGeneratorType(ItemStack item);

	public int getGeneratorStack(Location location);

	public EntityType getGeneratorType(Location location);
	
	public boolean isGenerator(ItemStack item);
}
