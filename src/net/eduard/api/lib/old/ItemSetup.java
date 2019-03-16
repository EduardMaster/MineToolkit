package net.eduard.api.lib.old;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.eduard.api.lib.game.Item;
/**
 * Extensão da classe {@link ItemStack} com mais métodos<br>
 * Versão nova {@link net.eduard.api.lib.old.Item} 2.0
 * 
 * @deprecated Versão atual {@link Item}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
@Deprecated
public class ItemSetup extends ItemStack implements ItemManager {
	public <E extends ItemStack> ItemSetup(E item) {
		copy(item);
	}

	public ItemSetup(Material type) {
		this(type, 1);
	}

	public ItemSetup(Material type, int amount) {
		this(type, amount, (short) 0);
	}

	public ItemSetup(Material type, int amount, short data) {
		this(type, amount, data, null);
	}

	public ItemSetup(Material type, int amount, short data, String name) {
		this(type, amount, data, name, "");
	}

	public ItemSetup(Material type, int amount, short data, String name, String... lore) {
		this(new ItemStack(type, amount, data));

		ItemMeta meta = getItemMeta();
		if (name != null) {
			meta.setDisplayName(name);
		}
		if (lore != null) {
			meta.setLore(Arrays.asList(lore));
		}
		setItemMeta(meta);
	}

	public <E extends ItemStack> void copy(E item) {
		setType(item.getType());
		setDurability(item.getDurability());
		setAmount(item.getAmount());
		setItemMeta(item.getItemMeta());
	}

	public String getName() {
		if (hasItemMeta())
			return getItemMeta().getDisplayName();
		return null;
	}

	public boolean hasName() {
		if (hasItemMeta())
			return getItemMeta().hasDisplayName();
		return false;
	}

	public void setLore(String... lore) {
		ItemMeta meta = getItemMeta();
		meta.setLore(Arrays.asList(lore));
		setItemMeta(meta);
	}

	public void setName(String name) {
		ItemMeta meta = getItemMeta();
		meta.setDisplayName(name);
		setItemMeta(meta);
	}
}
