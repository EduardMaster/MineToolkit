package net.eduard.api.lib.inventory;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.storage.Storable;

public class ItemRandom implements Storable {

	private int minAmount = 1;

	private int maxAmount = 1;

	private double chance = 1.0;

	private ItemStack item;

	public ItemRandom() {
	}

	public ItemRandom(ItemStack item, int min, int max) {
		this(item, min, max, 1);
	}

	public ItemRandom(ItemStack item, int min) {
		this(item, min, min);
	}

	public ItemRandom(ItemStack item, int min, int max, double chance) {
		setMinAmount(min);
		setMaxAmount(max);
		setChance(chance);
		setItem(item);
	}

	public ItemStack createChance() {
		if (Mine.getChance(chance)) {
			
			return createAmountRandom();
		}
		return new ItemStack(Material.AIR);

	}

	public ItemStack createAmountRandom() {
		ItemStack clone = item.clone();
		int amount = Mine.getRandomInt(getMinAmount(), getMaxAmount());

		clone.setAmount(amount);
		return clone;

	}

	public double getChance() {

		return chance;
	}

	public int getMaxAmount() {

		return maxAmount;
	}

	public int getMinAmount() {

		return minAmount;
	}

	public ItemStack getItem() {

		return this.item;
	}

	public void setItem(ItemStack item) {

		this.item = item;
	}

	public void setChance(double chance) {

		this.chance = chance;
	}

	public void setMaxAmount(int maxAmount) {

		this.maxAmount = maxAmount;
	}

	public void setMinAmount(int minAmount) {

		this.minAmount = minAmount;

	}


}
