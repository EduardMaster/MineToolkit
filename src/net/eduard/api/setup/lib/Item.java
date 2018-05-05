package net.eduard.api.setup.lib;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class Item extends ItemStack{

	@SuppressWarnings("deprecation")
	public Item(int id) {
		super(id);
	}

	@SuppressWarnings("deprecation")
	public Item() {
		super(1);
	}
	public Item newBook(String title,String author,String... pages) {
		
		if (this.getType() != Material.WRITTEN_BOOK) {
			this.setType(Material.WRITTEN_BOOK);
		}
		BookMeta meta = (BookMeta) getItemMeta();
		meta.addPage(pages);
		meta.setAuthor(author);
		meta.setTitle(title);
		setItemMeta(meta);
		return this;
	}
	public Item storeEnchant(Enchantment enchant, int level  ) {
		if (this.getType()!= Material.ENCHANTED_BOOK) {
			setType(Material.ENCHANTED_BOOK);
		}
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) getItemMeta();
		meta.addStoredEnchant(enchant, level, true);
		setItemMeta(meta);
		return this;
	}

	@SuppressWarnings("deprecation")
	public Item(int id, int amount, int data, String name, String... lore) {
		setTypeId(id);
		setAmount(amount);
		setDurability((short) data);
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(name);
		;
		meta.setLore(Arrays.asList(lore));
		;
		this.setItemMeta(meta);
	}

	public Item(Material type, int amount) {
		
		super(type, amount);
	}

	public Item(Material type) {
		super(type);
	}

	

	public Item addEnchant(Enchantment enchant, int level) {
		ItemMeta meta = getItemMeta();
		meta.addEnchant(enchant, level, true);
		setItemMeta(meta);
		return this;
	}

}

