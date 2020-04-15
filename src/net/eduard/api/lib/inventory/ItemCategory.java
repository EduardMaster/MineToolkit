package net.eduard.api.lib.inventory;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Sistema de categorização de Itens baseado no seu {@link Material}
 * 
 * @author Eduard
 * @version 1.0
 *
 */
public enum ItemCategory {

	WEAPON, ARMOUR, BLOCK, ORE, TOOL, OTHER, FARM, FOOD, SPAWNER, SKULL, POTION, ENCHANTED_BOOK;

	private ItemCategory() {

	}

	public static final List<Material> ARMOURS = getArmourTypes();
	public static final List<Material> WEAPONS = getWeaponTypes();
	public static final List<Material> ORES = getOreTypes();
	public static final List<Material> FARM_DROPS = getFarmTypes();
	public static final List<Material> FOODS = getFoodTypes();
	public static final List<Material> TOOLS = getToolTypes();

	public static List<Material> getArmourTypes() {
		List<Material> types = new ArrayList<>();
		for (Material type : Material.values()) {
			String nome = type.name();
			if (nome.contains("HELMET") || nome.contains("CHESTPLATE") || nome.contains("BOOTS")
					|| nome.contains("LEGGINS")) {
				types.add(type);
			}
		}
		return types;

	}

	public static List<Material> getOreTypes() {
		List<Material> types = new ArrayList<>();
		for (Material type : Material.values()) {
			String nome = type.name();

			if (nome.contains("ORE") | type == Material.DIAMOND | type == Material.DIAMOND_BLOCK
					| type == Material.IRON_INGOT | type == Material.IRON_BLOCK | type == Material.LAPIS_BLOCK
					| type == Material.REDSTONE | type == Material.REDSTONE_BLOCK | type == Material.EMERALD
					| type == Material.EMERALD_BLOCK | type == Material.GOLD_INGOT | type == Material.GOLD_BLOCK
					| type == Material.COAL | type == Material.COAL_BLOCK) {
				types.add(type);
			}
		}
		return types;

	}

	public static List<Material> getToolTypes() {
		List<Material> types = new ArrayList<>();
		for (Material type : Material.values()) {
			String nome = type.name();
			if (nome.contains("AXE") | nome.contains("PICKAXE") | nome.contains("HOE") | nome.contains("SPADE")
					| type == Material.FLINT_AND_STEEL | type == Material.COMPASS) {
				types.add(type);
			}
		}
		return types;

	}

	public static List<Material> getFarmTypes() {
		List<Material> types = new ArrayList<>();
		for (Material type : Material.values()) {
//			String nome = type.name();
			if (type == Material.NETHER_STAR | type == Material.SEEDS | type == Material.STRING
					| type == Material.BLAZE_ROD | type == Material.GOLD_NUGGET | type == Material.CACTUS
					| type == Material.ROTTEN_FLESH | type == Material.BONE | type == Material.RAW_BEEF
					| type == Material.SLIME_BALL | type == Material.SLIME_BLOCK | type == Material.PRISMARINE_SHARD) {
				types.add(type);
			}
		}
		return types;

	}

	public static List<Material> getFoodTypes() {
		List<Material> types = new ArrayList<>();
		for (Material type : Material.values()) {
			String nome = type.name();

			if (type == Material.GOLDEN_APPLE | type == Material.BAKED_POTATO | nome.contains("COOK")
					| nome.contains("RAW")) {
				types.add(type);
			}
		}
		return types;

	}

	public static List<Material> getWeaponTypes() {
		List<Material> types = new ArrayList<>();
		for (Material type : Material.values()) {
			String nome = type.name();
			if (nome.contains("SWORD") | nome.contains("BOW")) {
				types.add(type);
			}
		}
		return types;

	}

	public static ItemCategory getBy(ItemStack item) {

		Material type = item.getType();
		if (type == Material.SKULL_ITEM) {
			return ItemCategory.SKULL;
		}
		if (type == Material.POTION) {
			return POTION;
		}
		if (type == Material.MOB_SPAWNER) {
			return SPAWNER;
		}
		if (TOOLS.contains(type)) {
			return ItemCategory.TOOL;
		}
		if (ARMOURS.contains(type)) {
			return ItemCategory.ARMOUR;
		}
		if (ORES.contains(type)) {
			return ItemCategory.ORE;
		}
		if (WEAPONS.contains(type)) {
			return ItemCategory.WEAPON;
		}

		if (FARM_DROPS.contains(type))

		{
			return ItemCategory.FARM;
		}
		if (FOODS.contains(type)) {
			return ItemCategory.FOOD;
		}

		return OTHER;

	}

}
