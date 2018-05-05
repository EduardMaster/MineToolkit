package net.eduard.api.setup.lib;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Extra;


public class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	public EnchantGlow(int id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return false;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public String getName() {
		return "Glow";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	public static Enchantment getGlow() {
		if (glow != null)
			return glow;
		if (Enchantment.getByName("Glow") != null)
			return Enchantment.getByName("Glow");
		try {
			Extra.setValue(Enchantment.class, "acceptingNew", true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		glow = new EnchantGlow(255);
		Enchantment.registerEnchantment(glow);
		return glow;
	}

	public static ItemStack addGlow(ItemStack item) {
		Enchantment glow = getGlow();

		if (!item.containsEnchantment(glow))
			item.addUnsafeEnchantment(glow, 1);

		return item;
	}

	public static ItemStack removeGlow(ItemStack item) {
		Enchantment glow = getGlow();

		if (item.containsEnchantment(glow))
			item.removeEnchantment(glow);

		return item;
	}

}

