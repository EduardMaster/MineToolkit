package net.eduard.api.lib.game;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

/**
 * API de Encantamento Invisivel ou Sem Nome (Glow)
 * 
 * @author Eduard
 * @version 1.1
 */
public class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	public EnchantGlow(){
		super(250);
	}
	public EnchantGlow(int id) {
		super(id);
	}


	public boolean canEnchantItem(ItemStack item) {
		return false;
	}


	public boolean conflictsWith(Enchantment other) {
		return false;
	}


	public EnchantmentTarget getItemTarget() {
		return null;
	}


	public int getMaxLevel() {
		return 10;
	}


	public String getName() {
		return "Glow";
	}


	public int getStartLevel() {
		return 1;
	}

	public static Enchantment getGlow() {
		if (glow != null)
			return glow;
		if (Enchantment.getByName("Glow") != null)
			return Enchantment.getByName("Glow");
		try {
			Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
			acceptingNew.setAccessible(true);
			acceptingNew.set(null, true);
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
