package net.eduard.api.lib.menu;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

	private ShopSorter sortType = ShopSorter.BUY_PRICE_ASC;

	private String messageBoughtItem = "§aVoce adquiriu $amount ($product) produto(s) da Loja!";

	private String messageWithoutBalance = "§cVoce não tem dinheiro suficiente!";
	private String messageWithoutPermission = "§cVoce não tem permissão para comprar este produto!";
	@StorageAttributes(reference = true)
	private CurrencyManager currency;

	public Shop() {
		this("Menu", 3);
	}

	public Shop copy() {
		return copy(this);
	}

	public void organize() {
		if (sortType == ShopSorter.BUY_PRICE_ASC) {
			Stream<Product> lista = getButtons().stream().filter(b -> b instanceof Product).map(b -> (Product) b)
					.sorted(Comparator.comparing(Product::getUnitBuyPrice));
			lista.forEach(new Consumer<Product>() {
				int id = 0;

				public void accept(Product t) {
					t.setIndex(id);
					id++;
				}
			});
		}

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

					if ((event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT)
							&& (product.getTradeType() == TradeType.BOTH
									|| product.getTradeType() == TradeType.BUYABLE)) {
						double priceUnit = product.getUnitBuyPrice();
						int amount = 1;
						if (product.getProduct() != null) {
							if (event.getClick() == ClickType.SHIFT_RIGHT) {
								amount = product.getProduct().getAmount();
								if (amount > 64) {
									amount = 64;
								}
							}
						}
						double priceFinal = priceUnit * amount;
						ProductTradeEvent evento = new ProductTradeEvent(player);
						evento.setProduct(product);
						evento.setAmount(amount);
						if (useVault)
							evento.setBalance(VaultAPI.getEconomy().getBalance(player));
						else if (currency != null) {
							evento.setBalance(currency.getBalance(new FakePlayer(player)));
						}

						evento.setType(TradeType.BUYABLE);
						evento.setPriceTotal(priceFinal);
						evento.setShop(Shop.this);

						Mine.callEvent(evento);
						if (evento.isCancelled()) {
							return;
						}
						if (useVault && VaultAPI.hasVault() && VaultAPI.hasEconomy()) {

							if (VaultAPI.getEconomy().has(player, evento.getPriceTotal())) {
								VaultAPI.getEconomy().withdrawPlayer(player, evento.getPriceTotal());
							} else {
								player.sendMessage(messageWithoutBalance);
								return;
							}

						} else if (currency != null) {
							FakePlayer fake = new FakePlayer(player);

							if (currency.getBalance(fake) >= evento.getPriceTotal()) {
								currency.removeBalance(fake, evento.getPriceTotal());
							} else {
								player.sendMessage(messageWithoutBalance);
								return;
							}

						} else {
							log("§b[Shop] §cnao funcionado pois nao tem  um sistema de economia");
							return;
						}
						for (String cmd : product.getCommands()) {
							Mine.runCommand(cmd.replace("$player", player.getName()));
						}
						if (product.getProduct() == null) {
							return;
						}
						player.sendMessage(messageBoughtItem.replace("$amount", "" + amount).replace("$product",
								"" + product.getName()));

						if (Mine.isFull(player.getInventory())) {
							player.getWorld().dropItemNaturally(player.getLocation().add(0, 5, 0),
									product.getProduct());
						} else {
							ItemStack clone = product.getProduct().clone();
							clone.setAmount(amount);
							player.getInventory().addItem(clone);
						}

					}

					if ((event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT)
							&& (product.getTradeType() == TradeType.BOTH
									|| product.getTradeType() == TradeType.SELABLE)) {
						if (product.getProduct() != null) {
							double priceUnit = product.getUnitSellPrice();
							int amount = Mine.getTotalAmount(player.getInventory(), product.getProduct());
							if (event.getClick() == ClickType.LEFT) {
								if (amount > 64) {
									amount = 64;
								}
							}
							double finalPrice = amount * priceUnit;
							ProductTradeEvent evento = new ProductTradeEvent(player);
							evento.setProduct(product);
							evento.setAmount(amount);
							if (useVault)
								evento.setBalance(VaultAPI.getEconomy().getBalance(player));
							else if (currency != null) {
								evento.setBalance(currency.getBalance(new FakePlayer(player)));
							}

							evento.setType(TradeType.SELABLE);
							evento.setPriceTotal(finalPrice);
							evento.setShop(Shop.this);

							Mine.callEvent(evento);
							if (evento.isCancelled()) {
								return;
							}
							Mine.remove(player.getInventory(), product.getProduct());
							if (useVault && VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
								VaultAPI.getEconomy().depositPlayer(player, finalPrice);
							} else if (currency != null) {
								FakePlayer fake = new FakePlayer(player);
								currency.addBalance(fake, finalPrice);
							}
						}

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

	public ShopSorter getSortType() {
		return sortType;
	}

	public void setSortType(ShopSorter sortType) {
		this.sortType = sortType;
	}

}
