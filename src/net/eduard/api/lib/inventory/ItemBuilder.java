package net.eduard.api.lib.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Builder de Item que extende {@link ItemStack}
 * <br>Não é um padrão comum de Java
 * <br>
 * Método build() gerá um instancia nova de ItemStack
 *
 * @author Eduard
 */
public class ItemBuilder extends ItemStack {
    public ItemBuilder() {
        this(Material.STONE, 1);

    }

    public ItemBuilder(Material type) {
        this(type, 1);
    }

    public ItemBuilder(Material type, int amount) {
        setType(type);
        setAmount(amount);
    }

    public ItemBuilder(String name) {
        this();
        name(name);
    }

    public ItemBuilder skull(String skullName) {
        type(Material.SKULL_ITEM);
        data(SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) getItemMeta();
        meta.setOwner(skullName);
        setItemMeta(meta);
        return this;

    }

    public ItemBuilder amount(int amount) {
        setAmount(amount);
        return this;
    }

    public ItemBuilder data(int data) {
        setDurability((short) data);
        return this;
    }

    public ItemBuilder type(Material type) {

        setType(type);

        return this;
    }

    public ItemBuilder clearEnchants() {

        for (Enchantment enchant : getEnchantments().keySet()) {
            removeEnchantment(enchant);
        }

        return this;

    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(Collection<String> lore) {

        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>(lore));
        setItemMeta(meta);
        return this;
    }

    public List<String> getLore() {
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            return meta.getLore();
        }
        return new ArrayList<>();
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = getLore();
        lore.add(line);
        return lore(lore);
    }

    public ItemBuilder removeLore() {

        return lore();
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>());
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder glowed() {
        addEnchant(EnchantGlow.getGlow(), 1);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchant, int level) {

        addUnsafeEnchantment(enchant, level);

        return this;
    }

    public ItemBuilder name(String name) {

        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(name);
        setItemMeta(meta);
        return this;

    }


    public ItemStack builder() {
        return this.clone();
    }


}