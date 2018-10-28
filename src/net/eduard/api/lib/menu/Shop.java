package net.eduard.api.lib.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CurrencyManager;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.StorageAttributes;

public class Shop extends Menu {
	public Product getProductByIcon(ItemStack icon) {
		for (MenuButton button : getButtons()) {
			if (button instanceof Product) {

				Product product = (Product) button;
				if (product.getIcon().isSimilar(icon))
					return product;
			}

		}
		return null;
	}

	public Product getProduct(ItemStack item) {
		for (MenuButton button : getButtons()) {
			if (button instanceof Product) {

				Product product = (Product) button;
				if (product.getProduct().isSimilar(item))
					return product;
			}

		}
		return null;
	}

	public Shop() {
		this("Menu",3);
	}

	public Shop(String name, int lineAmount) {
		setEffect(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {

				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
					if (event.getCurrentItem() == null)
						return;
					Product product = getProductByIcon(event.getCurrentItem());
					if (product == null)
						return;
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
					if (product.getProduct() == null)
						return;
					player.sendMessage(messageBoughtItem);
					if (Mine.isFull(player.getInventory())) {
						player.getWorld().dropItemNaturally(player.getLocation().add(0, 5, 0), product.getProduct());
					} else {
						player.getInventory().addItem(product.getProduct());
					}
				}

			}

		});

		// TODO Auto-generated constructor stub
	}

	private boolean useVault = true;
	@StorageAttributes(reference=true)
	private CurrencyManager currency;
	private String messageBoughtItem = "§aVoce adquiriu um item da Loja!";
	private String messageWithoutBalance = "§cVoce não tem dinheiro suficiente!";

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
		if (currency == null)
			return;
		this.useVault = false;
		this.currency = currency;
		for (MenuButton button : getButtons()) {
			if (button.getMenu() instanceof Shop) {
				Shop shop = (Shop) button.getMenu();
				shop.setCurrency(currency);

			}

		}
	}
}
