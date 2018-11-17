package net.eduard.api.lib.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CurrencyManager;
import net.eduard.api.lib.modules.ClickEffect;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.StorageAttributes;

@StorageAttributes(indentificate = true)
public class Shop extends Menu {
	private boolean useVault = true;

	@StorageAttributes(reference = true)
	private CurrencyManager currency;

	private String messageBoughtItem = "§aVoce adquiriu $amount ($product) produto(s) da Loja!";

	private String messageWithoutBalance = "§cVoce não tem dinheiro suficiente!";
	private String messageWithoutPermission = "§cVoce não tem permissão para comprar este produto!";

	public Shop() {
		this("Menu", 3);
	}

	public Shop(String title, int lineAmount) {
		super(title, lineAmount);
		setEffect(new ClickEffect() {

			@Override
			public void onClick(InventoryClickEvent event, int page) {
				if (event.getWhoClicked() instanceof Player) {
					Player player = (Player) event.getWhoClicked();
					if (event.getCurrentItem() == null)
						return;

					Product product = getProduct(event.getCurrentItem());
					if (product == null) {
						return;
					}
					double price = product.getPrice();
					int amount = 1;
//					System.out.println("Preco "+price);
					if (event.getClick() == ClickType.SHIFT_RIGHT) {
						amount = 64;
					}
					if (event.getClick() == ClickType.MIDDLE) {
						int espacoVasio = Mine.getEmptySlotsAmount(player.getInventory());

						amount = espacoVasio * 64;
					}
					if (product.getPermission() != null) {
						if (!player.hasPermission(product.getPermission())) {
							player.sendMessage(messageWithoutPermission);
							return;
						}
					}
					price *= amount;
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

					} else {
//						Mine.console("§b[Shop] §cnao funcionado pois nao tem  um sistema de economia");
						return;
					}
//				
					for (String cmd : product.getCommands()) {
						Mine.runCommand(cmd.replace("$player", player.getName()));
					}
					if (product.getProduct() == null) {
						return;
					}
					player.sendMessage(messageBoughtItem.replace("$amount", "" + amount).replace("$product",
							"" + product.getName()));

					if (Mine.isFull(player.getInventory())) {
						player.getWorld().dropItemNaturally(player.getLocation().add(0, 5, 0), product.getProduct());
					} else {
						ItemStack clone = product.getProduct().clone();
						clone.setAmount(amount);
						player.getInventory().addItem(clone);
					}
				}

			}

		});

		// TODO Auto-generated constructor stub
	}

	public CurrencyManager getCurrency() {
		return currency;
	}

	public String getMessageBoughtItem() {
		return messageBoughtItem;
	}

	public String getMessageWithoutBalance() {
		return messageWithoutBalance;
	}

	public Product getProduct(ItemStack icon) {
		MenuButton button = getButton(icon);
		if (button != null) {
			if (button instanceof Product) {
				Product product = (Product) button;
				return product;
			}
		}
		return null;
	}

	public Product getProductFrom(ItemStack item) {
		for (MenuButton button : getButtons()) {
			if (button instanceof Product) {
				Product product = (Product) button;
				if (product.getItem().isSimilar(item))
					return product;
			}

		}
		return null;
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

	public void setMessageBoughtItem(String messageBoughtItem) {
		this.messageBoughtItem = messageBoughtItem;
	}

	public void setMessageWithoutBalance(String messageWithoutBalance) {
		this.messageWithoutBalance = messageWithoutBalance;
	}

	public void setUseVault(boolean useVault) {
		this.useVault = useVault;
	}

	public boolean useVault() {
		return useVault;
	}

	public String getMessageWithoutPermission() {
		return messageWithoutPermission;
	}

	public void setMessageWithoutPermission(String messageWithoutPermission) {
		this.messageWithoutPermission = messageWithoutPermission;
	}
}
