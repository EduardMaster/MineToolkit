package net.eduard.api.lib.game;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.manager.EffectManager;


public class Slot extends EffectManager {

	private ItemStack item;
	private int index;

	public Slot(ItemStack item, int index) {
		setItem(item);
		setIndex(index);
	}

	public int getIndex() {
		return index;
	}

	public Slot() {
		// TODO Auto-generated constructor stub
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void give(Inventory inventory) {
		inventory.setItem(index, item);
	}

}
