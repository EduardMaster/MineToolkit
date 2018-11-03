package net.eduard.api.lib.modules;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Criador de Encantamentos
 * 
 * @author Eduard
 * @version 1.0
 */
public abstract class CustomEnchant extends EnchantmentWrapper {

	private String name;
	private boolean registred;
	private int maxLevel = 1;
	private int startLevel = 1;

	@SuppressWarnings("deprecation")
	public CustomEnchant unregister() {
		if (isRegistred()) {
			try {
				Field byIdField = Enchantment.class.getDeclaredField("byId");
				Field byNameField = Enchantment.class.getDeclaredField("byName");
				byIdField.setAccessible(true);
				byNameField.setAccessible(true);
				Map<?, ?> byId = (Map<?, ?>) byIdField.get(null);
				Map<?, ?> byName = (Map<?, ?>) byNameField.get(null);
				byId.remove(getId());
				byName.remove(getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			setRegistred(false);
		}
		return this;
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	public ItemStack enchant(ItemStack item, int level) {

		item.addUnsafeEnchantment(this, level);
		String romano = convertToRomano(level);
		ItemMeta meta = item.getItemMeta();
		String enchamentname = "§7" + getName() + " " + romano;
		if (meta.getLore() == null) {

			meta.setLore(Arrays.asList(enchamentname));
		} else {
			List<String> lore = meta.getLore();
			lore.add(0, enchamentname);
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		return item;
	}

	public static String convertToRomano(int level) {
		String romano = "I";
		switch (level) {
		case 2:
			romano = "II";
			break;
		case 3:
			romano = "III";
			break;
		case 4:
			romano = "IV";
			break;
		case 5:
			romano = "V";
			break;
		}
		return romano;
	}

	public CustomEnchant register() {
		setRegistred(true);
		try {
			try {
				Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
				acceptingNew.setAccessible(true);
				acceptingNew.set(null, true);
				Enchantment.registerEnchantment(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;

	}

	public CustomEnchant(int id, String name) {
		super(id);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public boolean isRegistred() {
		return registred;
	}

	public void setRegistred(boolean registred) {
		this.registred = registred;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public int getStartLevel() {
		return startLevel;
	}

	public void setStartLevel(int startLevel) {
		this.startLevel = startLevel;
	}

}
