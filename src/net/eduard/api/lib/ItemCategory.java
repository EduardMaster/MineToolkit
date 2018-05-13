package net.eduard.api.lib;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public enum ItemCategory {

	WEAPON, ARMOUR, BLOCK, ORE, TOOL, OTHER, FARM, FOOD, SPAWNER, SKULL, POTION;

	public static ItemCategory getBy(ItemStack item) {
		if (Enchantment.DAMAGE_ALL.canEnchantItem(item)) {
			return ItemCategory.WEAPON;
		}
		if (item.getType() == Material.DIAMOND_ORE | item.getType() == Material.DIAMOND
				| item.getType() == Material.DIAMOND_BLOCK | item.getType() == Material.IRON_ORE
				| item.getType() == Material.IRON_INGOT | item.getType() == Material.IRON_BLOCK
				| item.getType() == Material.LAPIS_BLOCK | item.getType() == Material.LAPIS_ORE
				| item.getType() == Material.REDSTONE | item.getType() == Material.REDSTONE_BLOCK
				| item.getType() == Material.REDSTONE_ORE | item.getType() == Material.EMERALD
				| item.getType() == Material.EMERALD_ORE | item.getType() == Material.EMERALD_BLOCK
				| item.getType() == Material.GOLD_INGOT | item.getType() == Material.GOLD_ORE
				| item.getType() == Material.GOLD_BLOCK | item.getType() == Material.COAL
				| item.getType() == Material.COAL_ORE | item.getType() == Material.COAL_BLOCK) {
			return ItemCategory.ORE;
		} else if (item.getType().name().contains("AXE") | item.getType().name().contains("PICKAXE")
				| item.getType().name().contains("HOE") | item.getType().name().contains("SPADE")
				| item.getType() == Material.FLINT_AND_STEEL | item.getType() == Material.COMPASS) {

			return ItemCategory.TOOL;

		}
		if (item.getType().name().contains("CHESTPLATE") | item.getType().name().contains("HELMET")
				| item.getType().name().contains("BOOTS") | item.getType().name().contains("LEGGINGS")) {
			return ARMOUR;
		} else if (item.getType() == Material.NETHER_STAR | item.getType() == Material.SEEDS
				| item.getType() == Material.STRING | item.getType() == Material.BLAZE_ROD
				| item.getType() == Material.GOLD_NUGGET | item.getType() == Material.CACTUS
				| item.getType() == Material.ROTTEN_FLESH | item.getType() == Material.BONE
				| item.getType() == Material.RAW_BEEF | item.getType() == Material.SLIME_BALL
				| item.getType() == Material.SLIME_BLOCK | item.getType() == Material.PRISMARINE_SHARD) {
			return ItemCategory.FARM;
		}
		if (item.getType() == Material.GOLDEN_APPLE) {
			return ItemCategory.FOOD;
		}
		if (item.getType() == Material.SKULL_ITEM) {
			return ItemCategory.SKULL;
		}
		if (item.getType() == Material.MOB_SPAWNER) {
			return SPAWNER;
		}
		if (item.getType() == Material.POTION) {
			return POTION;
		}
		return OTHER;

	}

}

