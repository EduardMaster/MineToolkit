package net.eduard.api.lib.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Sistema de contruir o Item e o item é a mesma classe que o Builder
 * 
 * @author Eduard
 *
 */
public class ItemBuilder extends ItemStack {
	public ItemBuilder() {
		setType(Material.STONE);
		setAmount(1);

	}

	public ItemBuilder(Material type) {
		setType(type);
	}

	public ItemBuilder(Material type, int amount) {
		setType(type);
		setAmount(amount);
	}

	public ItemBuilder(String name) {
		setType(Material.STONE);
		setName(name);
	}

	public ItemBuilder amount(int amount) {
		setAmount(amount);
		return this;
	}

	public ItemBuilder setData(int data) {
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

	public ItemBuilder setLore(String... lore) {

		ItemMeta meta = getItemMeta();
		meta.setLore(Arrays.asList(lore));
		setItemMeta(meta);
		return this;
	}

	public List<String> getLore() {
		ItemMeta meta = getItemMeta();
		return meta.getLore();
	}

	public ItemBuilder addLore(String line) {

		ItemMeta meta = getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new ArrayList<>();
		}
		lore.add(line);
		meta.setLore(lore);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeLore() {
		ItemMeta meta = getItemMeta();
		meta.setLore(null);
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder clearLore() {
		ItemMeta meta = getItemMeta();
		meta.setLore(new ArrayList<>());
		setItemMeta(meta);
		return this;
	}

	public ItemBuilder addEnchant(Enchantment enchant, int level) {

		addUnsafeEnchantment(enchant, level);

		return this;
	}

	public ItemBuilder setName(String name) {

		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(name);
		setItemMeta(meta);
		return this;

	}

}
