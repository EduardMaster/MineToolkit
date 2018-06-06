package net.eduard.api.lib.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.VaultAPI;
import net.eduard.api.lib.core.Mine;

public class ShopGui extends Menu {

	private ItemStack[] products;
	private double[] prices;

	public ShopGui() {
		// TODO Auto-generated constructor stub
	}

	public ShopGui(String name, int lines) {
		super(name, lines);
		setClick(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
					int slot = event.getRawSlot();
					ItemStack product = getProduct(page, slot);
					double price = getPrice(page, slot);
					if (product != null) {
						if (VaultAPI.hasVault()) {
							if (VaultAPI.hasEconomy()) {

								if (VaultAPI.getEconomy().has(player, price)) {
									VaultAPI.getEconomy().withdrawPlayer(player, price);
									if (Mine.isFull(player.getInventory())) {
										player.getWorld().dropItemNaturally(player.getLocation().add(0, 5, 0),
												product);
									} else {
										player.getInventory().addItem(product);
									}
									player.sendMessage("§aVoce adquiriu um item da Loja!");
								} else {
									player.sendMessage("§cVoce n§o tem dinheiro suficiente!");
								}

							}
						}
					}

				}
			}
		});

	}

	public void resetInventories() {
		super.resetInventories();
		prices = new double[getLines() * 9 * getPages()];
		products = new ItemStack[getLines() * 9 * getPages()];
	}

	/**
	 * 
	 * @param product
	 * @param page
	 * @param slot
	 * @param price
	 */
	public void addProduct(ItemStack product, int page, int slot, double price) {
		int index = getIndex(page, slot);
		List<String> lore = new ArrayList<String>();
		ItemStack clone = product.clone();
		if (clone.getItemMeta().hasLore())
			lore.addAll(clone.getItemMeta().getLore());
		lore.add("");
		lore.add("§aPre§o: §2" + price);
		lore.add("");
		Mine.setLore(clone, lore);
		addSlot(clone, page, slot);
		products[index] = product;
		prices[index] = price;
	}

	public void removeProduct(int page, int slot) {
		int index = getIndex(page, slot);
		products[index] = null;
		prices[index] = 0;
		removeSlot(page, slot);

	}

	public ItemStack getProduct(int page, int slot) {
		int index = getIndex(page, slot);
		return products[index];
	}

	public double getPrice(int page, int slot) {
		int index = getIndex(page, slot);
		return prices[index];
	}

}
