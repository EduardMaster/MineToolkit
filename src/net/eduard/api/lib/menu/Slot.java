package net.eduard.api.lib.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.manager.EffectManager;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

public class Slot implements Storable,Copyable {
	private int positionX, positionY;
	private ItemStack item;
	private EffectManager effects;
	
	public void setSlot(Slot slot)  {
		setIndex(slot.getIndex());
		setItem(slot.getItem());
	}
	
	public Slot copy() {
		return copy(this);
	}

	public Slot(ItemStack item, int index) {
		setItem(item);
		setIndex(index);
	}

	public int getIndex() {
		return Extra.getIndex(positionX, positionY);
	}
	public int getSlot() {
		return getIndex();
	}

	public Slot() {
		// TODO Auto-generated constructor stub
	}
	public boolean equals(ItemStack item) {
		return this.item.equals(item);
	}

	public Slot(ItemStack item, int positionX, int positionY) {
		setPositionX(positionX);
		setPositionY(positionY);
		setItem(item);
		// TODO Auto-generated constructor stub
	}

	public void setIndex(int index) {
		setPositionX(Extra.getColumn(index));
		setPositionY(Extra.getLine(index));
	}

	public void setPosition(int collumn, int line) {
		setPositionX(collumn);
		setPositionY(line);
	}

	public ItemStack getItem() {

		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void give(Inventory inventory) {
		inventory.setItem(getIndex(), item);
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public EffectManager getEffects() {
		return effects;
	}

	public void setEffects(EffectManager effects) {
		this.effects = effects;
	}

}