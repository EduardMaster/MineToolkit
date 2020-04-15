package net.eduard.api.lib.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;
@StorageAttributes(inline=true)
public class Item implements Storable {

	private int id;
	private int data;
	private int amount;
	private String name;
	private List<String> lore = new ArrayList<>();
	private Map<Integer, Integer> enchants = new HashMap<>();

	public Item() {
		// TODO Auto-generated constructor stub
	}
	public Item(int type) {
		setId(type);
	}

	@SuppressWarnings("deprecation")
	public ItemStack create() {
		ItemStack item = new ItemStack(id, amount, (short) data);
		Mine.setName(item, name);
		Mine.setLore(item, lore);
		enchants.entrySet().forEach(e -> {
			Mine.addEnchant(item, Enchantment.getById(e.getKey()), e.getValue());
		});
		return item;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLore() {
		return lore;
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public Map<Integer, Integer> getEnchants() {
		return enchants;
	}

	public void setEnchants(Map<Integer, Integer> enchants) {
		this.enchants = enchants;
	}

	// public Item newBook(String title,String author,String... pages) {
	//
	// if (this.getType() != Material.WRITTEN_BOOK) {
	// this.setType(Material.WRITTEN_BOOK);
	// }
	// BookMeta meta = (BookMeta) getItemMeta();
	// meta.addPage(pages);
	// meta.setAuthor(author);
	// meta.setTitle(title);
	// setItemMeta(meta);
	// return this;
	// }
	// public Item storeEnchant(Enchantment enchant, int level ) {
	// if (this.getType()!= Material.ENCHANTED_BOOK) {
	// setType(Material.ENCHANTED_BOOK);
	// }
	// EnchantmentStorageMeta meta = (EnchantmentStorageMeta) getItemMeta();
	// meta.addStoredEnchant(enchant, level, true);
	// setItemMeta(meta);
	// return this;
	// }

	// public Item addEnchant(Enchantment enchant, int level) {
	// ItemMeta meta = getItemMeta();
	// meta.addEnchant(enchant, level, true);
	// setItemMeta(meta);
	// return this;
	// }

}
