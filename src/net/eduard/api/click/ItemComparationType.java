package net.eduard.api.click;

import org.bukkit.inventory.ItemStack;

public enum ItemComparationType {

	BY_NAME, BY_TYPE_NAME, BY_TYPE, BY_SIMILIARITY, BY_EQUALITY;

	public boolean compare(ItemStack item, ItemStack other) {
		if (item == null)
			return false;
		if (other == null)
			return false;
		switch (this) {
			case BY_NAME :
				return item.getItemMeta().getDisplayName()
						.equals(other.getItemMeta().getDisplayName());
			case BY_SIMILIARITY :
				return item.isSimilar(other);
			case BY_EQUALITY :
				return item.equals(other);
			case BY_TYPE :
				return item.getType() == other.getType();
			case BY_TYPE_NAME :
				return item.getType().name().toLowerCase()
						.contains(other.getType().name().toLowerCase());
		}

		return false;
	}
}
