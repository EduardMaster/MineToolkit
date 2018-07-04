package net.eduard.api.lib.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.Storable;

public class KitUpgrade implements Storable {
	private double price;
	private int level;
	private List<ItemStack> items = new ArrayList<>();

	public double getPrice() {
		return price;
	}

	public KitUpgrade() {
		// TODO Auto-generated constructor stub
	}

	public KitUpgrade(double price, int level) {
		super();
		this.price = price;
		this.level = level;
	}

	public KitUpgrade(Kit kit,double price, int level) {
		super();
		kit.getUpgrades().add(this);
		this.price = price;
		this.level = level;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}

}
