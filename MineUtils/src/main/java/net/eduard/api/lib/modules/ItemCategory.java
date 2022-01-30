package net.eduard.api.lib.modules;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Categorizador de Itens baseado no seu Material
 *
 * @author Eduard
 * @version 1.0
 */
@SuppressWarnings("unused")
public enum ItemCategory {

    WEAPON,
    ARMOUR,
    BLOCK,
    ORE,
    TOOL,
    OTHER,
    FARM,
    FOOD,
    SPAWNER,
    SKULL,
    POTION,
    ENCHANTED_BOOK;

    ItemCategory() {

    }

    public static final Set<Material> ARMOURS = getArmourTypes();
    public static final Set<Material> WEAPONS = getWeaponTypes();
    public static final Set<Material> ORES = getOreTypes();
    public static final Set<Material> FARM_DROPS = getFarmTypes();
    public static final Set<Material> FOODS = getFoodTypes();
    public static final Set<Material> TOOLS = getToolTypes();

    public static Set<Material> getArmourTypes() {
        Set<Material> types = new HashSet<>();
        for (Material type : Material.values()) {
            String nome = type.name();
            if (nome.contains("HELMET") ||
                    nome.contains("CHESTPLATE") ||
                    nome.contains("BOOTS") ||
                    nome.contains("LEGGINGS")) {
                types.add(type);
            }
        }
        return types;

    }

    public static Set<Material> getOreTypes() {
        Set<Material> types = new HashSet<>();
        types.add(Material.DIAMOND);
        types.add(Material.DIAMOND_BLOCK);
        types.add(Material.IRON_INGOT);
        types.add(Material.IRON_BLOCK);
        types.add(Material.LAPIS_BLOCK);
        types.add(Material.REDSTONE);
        types.add(Material.REDSTONE_BLOCK);
        types.add(Material.EMERALD);
        types.add(Material.EMERALD_BLOCK);
        types.add(Material.COAL_BLOCK);
        types.add(Material.COAL);
        types.add(Material.GOLD_BLOCK);
        types.add(Material.GOLD_INGOT);
        for (Material type : Material.values()) {
            String nome = type.name();
            if (nome.contains("ORE")) {
                types.add(type);
            }
        }
        return types;

    }

    public static Set<Material> getToolTypes() {
        Set<Material> types = new HashSet<>();
        types.add(Material.FLINT_AND_STEEL);
        types.add(Material.COMPASS);

        for (Material type : Material.values()) {
            String nome = type.name();
            if (nome.contains("AXE") |
                    nome.contains("PICKAXE") |
                    nome.contains("HOE") |
                    nome.contains("SPADE")
            ) {
                types.add(type);
            }
        }
        return types;

    }

    public static Set<Material> getFarmTypes() {
        Set<Material> types = new HashSet<>();
        types.add(Material.NETHER_STAR);
        types.add(Material.SEEDS);
        types.add(Material.STRING);
        types.add(Material.BLAZE_ROD);
        types.add(Material.GOLD_NUGGET);
        types.add(Material.CACTUS);
        types.add(Material.ROTTEN_FLESH);
        types.add(Material.BONE);
        types.add(Material.RAW_BEEF);
        types.add(Material.SLIME_BALL);
        types.add(Material.SLIME_BLOCK);
        types.add(Material.PRISMARINE_SHARD);
        return types;

    }

    public static Set<Material> getFoodTypes() {
        Set<Material> types = new HashSet<>();
        types.add(Material.GOLDEN_APPLE);
        types.add(Material.BAKED_POTATO);
        for (Material type : Material.values()) {
            String nome = type.name();
            if (nome.contains("COOK")
                    | nome.contains("RAW")) {
                types.add(type);
            }
        }
        return types;

    }

    public static Set<Material> getWeaponTypes() {
        Set<Material> types = new HashSet<>();
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
        if (type == Material.ENCHANTED_BOOK) {
            return ENCHANTED_BOOK;
        }
        if (type == Material.SKULL_ITEM) {
            return ItemCategory.SKULL;
        }
        if (type == Material.POTION ||
                type == Material.GLASS_BOTTLE) {
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

        if (FARM_DROPS.contains(type)) {
            return ItemCategory.FARM;
        }
        if (FOODS.contains(type)) {
            return ItemCategory.FOOD;
        }

        return OTHER;

    }

}
