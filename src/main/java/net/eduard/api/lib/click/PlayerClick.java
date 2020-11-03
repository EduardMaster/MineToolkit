package net.eduard.api.lib.click;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerClick extends PlayerInteract  {

	private PlayerClickEffect clickEffect;

	private ClickComparationType comparationType =ClickComparationType.WITH_RIGHT;

	public PlayerClick() {
		this(null);
	}
	public PlayerClick(ItemStack item) {
		super(item, true, false, true);
	}
	public PlayerClick(Material material, PlayerClickEffect clickEffect) {
		this(new ItemStack(material),clickEffect);
		setItemComparationType(ItemComparationType.BY_TYPE);
	}
	public PlayerClick(ItemStack item, PlayerClickEffect clickEffect) {
		this(item);
		setClickEffect(clickEffect);
	}
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL)
			return;
		if (!isInteractWithAnyItem()) {
			if (!getItemComparationType().compare(getItem(), event.getItem()))
				return;
		}
		if (!comparationType.compare(event.getAction().name()))
			return;
		event.setCancelled(true);
		clickEffect.onClick(event.getPlayer(), event.getClickedBlock(),
				getItem());
	}
	public PlayerClickEffect getClickEffect() {
		return clickEffect;
	}
	public void setClickEffect(PlayerClickEffect clickEffect) {
		this.clickEffect = clickEffect;
	}
	public ClickComparationType getComparationType() {
		return comparationType;
	}
	public void setComparationType(ClickComparationType comparationType) {
		this.comparationType = comparationType;
	}


}
