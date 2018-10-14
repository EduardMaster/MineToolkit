package net.eduard.api.lib.game;

import org.bukkit.inventory.ItemStack;

public class MenuProduct extends MenuButton{
	

	private ItemStack item;
	private double price;
	private String permission;
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public double getPricePerUnit() {
		return price/item.getAmount();
	}



	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}


}
