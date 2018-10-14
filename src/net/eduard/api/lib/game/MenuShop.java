package net.eduard.api.lib.game;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CurrencyManager;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.Reference;

public class MenuShop extends Menu {
	private boolean useVault = true;
	@Reference
	private CurrencyManager currency;
	private String messageBoughtItem = "�aVoce adquiriu um item da Loja!";
	private String messageWithoutBalance = "�cVoce n�o tem dinheiro suficiente!";

	public MenuShop() {
		this("Menu", 1);

	}

	public MenuShop(String name, int lines) {
		super(name, lines);
		setEffect(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
//					int slot = event.getRawSlot();
					ItemStack item = event.getCurrentItem();
					if (item == null)
						return;
					MenuProduct product = getProductByIcon(item);
					if (product != null) {
						if (product.isCategory()) {
							Mine.broadcast("�a� uma categoria ");
							return;
						}
						double price = product.getPrice();

						if (useVault && VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

							if (VaultAPI.getEconomy().has(player, price)) {
								VaultAPI.getEconomy().withdrawPlayer(player, price);
							} else {
								player.sendMessage(messageWithoutBalance);
								return;
							}

						} else if (currency != null) {
							FakePlayer fake = new FakePlayer(player);
							if (currency.getBalance(fake) >= price) {
								currency.removeBalance(fake, price);
							} else {
								player.sendMessage(messageWithoutBalance);
								return;
							}
						} else
							return;
						player.sendMessage(messageBoughtItem);
						if (Mine.isFull(player.getInventory())) {
							player.getWorld().dropItemNaturally(player.getLocation().add(0, 5, 0), product.getItem());
						} else {
							player.getInventory().addItem(product.getItem());
						}

					}
				}
			}
		});

	}

	public MenuProduct getProductByIcon(ItemStack icon) {
		for (MenuButton button : getButtons()) {
			if (button instanceof MenuProduct) {
				MenuProduct product = (MenuProduct) button;
				if (product.getIcon().equals(icon))
					return product;
			}
		}
		return null;
	}

	public MenuProduct getProductByItem(ItemStack item) {
		for (MenuButton button : getButtons()) {
			if (button instanceof MenuProduct) {
				MenuProduct product = (MenuProduct) button;
				if (product.getItem().equals(item))
					return product;
			}

		}
		return null;
	}

	public MenuProduct getProduct(String name) {
		for (MenuButton button : getButtons()) {
			if (button instanceof MenuProduct) {
				MenuProduct product = (MenuProduct) button;
				if (product.getName().equalsIgnoreCase(name))
					return product;
			}

		}
		return null;
	}

	public void addProduct(MenuProduct product) {
		addButton(product.getPage(), product);

	}

	public void removeProduct(MenuProduct product) {
		removeButton(product);
	}

	public String getMessageBoughtItem() {
		return messageBoughtItem;
	}

	public void setMessageBoughtItem(String messageBoughtItem) {
		this.messageBoughtItem = messageBoughtItem;
	}

	public String getMessageWithoutBalance() {
		return messageWithoutBalance;
	}

	public void setMessageWithoutBalance(String messageWithoutBalance) {
		this.messageWithoutBalance = messageWithoutBalance;
	}

	public boolean useVault() {
		return useVault;
	}

	public void setUseVault(boolean useVault) {
		this.useVault = useVault;
	}

	public CurrencyManager getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyManager currency) {
		this.currency = currency;
	}

}
