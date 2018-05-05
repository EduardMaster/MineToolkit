package net.eduard.api.setup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.config.ConfigSection;
import net.eduard.api.setup.StorageAPI.Storable;
import net.eduard.api.setup.lib.Item;

public class Kit implements Storable {

	private String name;
	private long cooldown;
	private double price;
	private ItemStack icon;
	private int level;
	private List<Kit> levels = new ArrayList<>();
	private List<ItemStack> items = new ArrayList<>();
	private List<Item> extras = new ArrayList<>();
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack boots;
	private ItemStack leggins;

	private boolean clearInventory;
	private boolean fillSoup;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemStack getIcon() {
		return icon;
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	public boolean isClearInventory() {
		return clearInventory;
	}

	public void setClearInventory(boolean clearInventory) {
		this.clearInventory = clearInventory;
	}

	public boolean isFillSoup() {
		return fillSoup;
	}

	public void setFillSoup(boolean fillSoup) {
		this.fillSoup = fillSoup;
	}

	public void save(ConfigSection section, Object value) {

	}

	public Object get(ConfigSection section) {
		return null;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<ItemStack> getItems() {
		return items;
	}

	public void setItems(List<ItemStack> items) {
		this.items = items;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	public List<Kit> getLevels() {
		return levels;
	}

	public void setLevels(List<Kit> levels) {
		this.levels = levels;
	}

	public List<Item> getExtras() {
		return extras;
	}

	public void setExtras(List<Item> extras) {
		this.extras = extras;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	public ItemStack getChestplate() {
		return chestplate;
	}

	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	public ItemStack getBoots() {
		return boots;
	}

	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	public ItemStack getLeggins() {
		return leggins;
	}

	public void setLeggins(ItemStack leggins) {
		this.leggins = leggins;
	}
}
