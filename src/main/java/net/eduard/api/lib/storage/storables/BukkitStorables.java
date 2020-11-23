package net.eduard.api.lib.storage.storables;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import net.eduard.api.lib.storage.StorageAPI;

public class BukkitStorables {

	public static void load() {
		StorageAPI.registerStorable(Chunk.class, new ChunkStorable());
		StorageAPI.registerStorable(World.class, new WorldStorable());
		StorageAPI.registerStorable(Vector.class, new VectorStorable());
		StorageAPI.registerStorable(ItemStack.class, new ItemStackStorable());
		StorageAPI.registerStorable(Enchantment.class, new EnchantmentStorable());
		StorageAPI.registerStorable(PotionEffect.class, new PotionEffectStorable());
		StorageAPI.registerStorable(Location.class, new LocationStorable());
		StorageAPI.registerStorable(OfflinePlayer.class, new OfflinePlayerStorable());
		StorageAPI.registerStorable(MaterialData.class, new MaterialDataStorable());
		StorageAPI.registerStorable(Inventory.class, new InventoryStorable());
	}

}
