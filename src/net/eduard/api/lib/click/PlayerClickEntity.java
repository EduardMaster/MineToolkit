package net.eduard.api.lib.click;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerClickEntity extends PlayerInteract {

	private PlayerClickEntityEffect clickEntityEffect;

	public PlayerClickEntity(ItemStack item) {
		super(item, true, true, false);
	}
	public PlayerClickEntity() {
		this(null);
	}
	public PlayerClickEntity(ItemStack item,
			PlayerClickEntityEffect clickEntityEffect) {
		this(item);
		setClickEntityEffect(clickEntityEffect);
	}

	public PlayerClickEntity(Material material,
			PlayerClickEntityEffect clickEntityEffect) {
		this(new ItemStack(material),clickEntityEffect);
		setItemComparationType(ItemComparationType.BY_TYPE);
	}

	@EventHandler
	public void onClickAtEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (!isInteractWithAnyItem()) {
			if (!getItemComparationType().compare(getItem(),
					event.getPlayer().getItemInHand()))
				return;
		}
		clickEntityEffect.onClickAtEntity(player, event.getRightClicked(),
				getItem());

	}

	public PlayerClickEntityEffect getClickEntityEffect() {
		return clickEntityEffect;
	}

	public void setClickEntityEffect(
			PlayerClickEntityEffect clickEntityEffect) {
		this.clickEntityEffect = clickEntityEffect;
	}



}

