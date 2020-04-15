package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.eduard.api.lib.inventory.ItemRandom;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.storage.Storable;

public class MinigameChest implements Storable {
	private int refilTime = 2 * 60;
	private int maxItems = 10;
	private boolean acumulateItems = true;
	private boolean randomItems = true;
	private boolean shuffleItems = true;
	private boolean noRepeatItems;
	private List<ItemRandom> items = new ArrayList<>();

	public void fill(Inventory inv) {
		if (!acumulateItems) {
			inv.clear();
		}
		for (int index = 0; index < maxItems; index++) {
			ItemStack item = null;
			ItemRandom itemrandom = null;
			if (randomItems) {
				itemrandom = Mine.getRandom(items);
			} else {
				if (index < items.size()) {
					itemrandom = items.get(index);
				}
			}
			if (itemrandom != null) {
				item = itemrandom.createChance();
			}
			if (item.getType() == Material.AIR) {
				index--;
			}
			inv.setItem(inv.firstEmpty(), item);
		}
		if (noRepeatItems) {
			ItemStack[] contents = inv.getContents();
			Stream<ItemStack> stream = Arrays.stream(contents);
			stream.distinct();
			contents=stream.toArray(size ->new ItemStack[size]);
			inv.setContents(contents);
		}
		if (shuffleItems) {
			ItemStack[] contents = inv.getContents();
			for (int i = 0; i < contents.length; i++) {
				ItemStack itemStack = contents[i];
				int temp = Mine.getRandomInt(1, contents.length);
				temp--;
				ItemStack tempItem = contents[temp];
				contents[i] = tempItem;
				contents[temp] = itemStack;
			}
			inv.setContents(contents);
		}

	}

	public int getRefilTime() {
		return refilTime;
	}

	public void setRefilTime(int refilTime) {

		this.refilTime = refilTime;
	}

	public List<ItemRandom> getItems() {
		return items;
	}

	public void setItems(List<ItemRandom> items) {
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

	public boolean isNoRepeatItems() {
		return noRepeatItems;
	}

	public void setNoRepeatItems(boolean noRepeatItems) {
		this.noRepeatItems = noRepeatItems;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public boolean isAcumulateItems() {
		return acumulateItems;
	}

	public void setAcumulateItems(boolean acumulateItems) {
		this.acumulateItems = acumulateItems;
	}

	public boolean isRandomItems() {
		return randomItems;
	}

	public void setRandomItems(boolean randomItems) {
		this.randomItems = randomItems;
	}

	public boolean isShuffleItems() {
		return shuffleItems;
	}

	public void setShuffleItems(boolean shuffleItems) {
		this.shuffleItems = shuffleItems;
	}

}
