package net.eduard.api.lib.menu;

import org.bukkit.inventory.ItemStack;

public class Product extends MenuButton {
	private double price;
	private String permission;
	private ItemStack product;

	public Product(ItemStack icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public Product(String name, ItemStack icon) {
		super(name, icon);
		// TODO Auto-generated constructor stub
	}

	public Product(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public ItemStack getProduct() {
		return product;
	}

	public void setProduct(ItemStack product) {
		this.product = product;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
