package net.eduard.api.lib.click;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.storage.Storable;

public abstract class PlayerInteract extends EventsManager implements  Storable{
	
	private ItemStack item;
	
	private ItemComparationType itemComparationType = ItemComparationType.BY_SIMILIARITY;
	
	private transient boolean interactWithAnyItem;
	
	private transient boolean interactWithItem;
	
	private transient boolean interactWithEntity;
	
	private transient boolean interactWithBlock;

	protected PlayerInteract(ItemStack item, boolean interactWithItem,
			boolean interactWithEntity, boolean interactWithBlock) {
		super();
		this.item = item;
		this.interactWithItem = interactWithItem;
		this.interactWithEntity = interactWithEntity;
		this.interactWithBlock = interactWithBlock;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	protected boolean isInteractWithItem() {
		return interactWithItem;
	}

	protected void setInteractWithItem(boolean interactWithItem) {
		this.interactWithItem = interactWithItem;
	}

	protected boolean isInteractWithEntity() {
		return interactWithEntity;
	}

	protected void setInteractWithEntity(boolean interactWithEntity) {
		this.interactWithEntity = interactWithEntity;
	}

	protected boolean isInteractWithBlock() {
		return interactWithBlock;
	}

	protected void setInteractWithBlock(boolean interactWithBlock) {
		this.interactWithBlock = interactWithBlock;
	}

	public ItemComparationType getItemComparationType() {
		return itemComparationType;
	}

	public void setItemComparationType(ItemComparationType itemComparationType) {
		this.itemComparationType = itemComparationType;
	}

	protected boolean isInteractWithAnyItem() {
		return interactWithAnyItem;
	}

	protected void setInteractWithAnyItem(boolean interactWithAnyItem) {
		this.interactWithAnyItem = interactWithAnyItem;
	}


}
