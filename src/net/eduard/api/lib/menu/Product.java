package net.eduard.api.lib.menu;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;

public class Product extends MenuButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double sellPrice;
	private double buyPrice;
	private boolean limited = false;
	private int stock;
	private TradeType tradeType = TradeType.BUYABLE;
	private String permission;
	private List<String> commands = new ArrayList<>();
	private ItemStack product;

	public int getAmount() {
		return product.getAmount();
	}

	public double getUnitSellPrice() {
		return sellPrice / getAmount();
	}

	public double getUnitBuyPrice() {
		return buyPrice / getAmount();
	}

	public ItemStack getItem() {
		DecimalFormat d = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.forLanguageTag("PT")));
//		System.out.println(getProduct());
		ItemStack clone = super.getItem();
		if (clone == null) {
			clone = getProduct();
		}
		clone = clone.clone();
		if (limited) {
			clone.setAmount(stock);
		}
		List<String> lore = Mine.getLore(clone);
		lore.add(" ");
		if (getTradeType() == TradeType.BUYABLE || getTradeType() == TradeType.BOTH) {
			lore.add("§fCompre o produto §e" + getName());
			if (limited) {
				if (stock == 0) {
					lore.add("§2Sem Estoque");
				} else {
					lore.add("§2Quantidade Restante: §a" + stock);
				}
			}
			lore.add("§2Preço por 1: §a" + d.format(getUnitBuyPrice()));
			lore.add("§2Preço por 64: §a" + d.format(64 * getUnitBuyPrice()));
		} else if (getTradeType() == TradeType.SELABLE || getTradeType() == TradeType.BOTH) {
			lore.add("§fVende o produto: §e" + getName());
			if (limited) {
				if (stock == 0) {
					lore.add("§cSem Estoque");
				} else {
					lore.add("§2Quantidade Restante: §a" + stock);
				}
			}
			lore.add("§2Preço por 64: §a" + d.format(64 * getUnitSellPrice()));
			lore.add("§2Preço por Inventario: §a" + d.format(64 * 9 * 4 * getUnitSellPrice()));
		}

		Mine.setLore(clone, lore);
		return clone;
	}

	public Product(ItemStack icon) {
		super(icon);
		setName("Produto");
	}

	public void setItem(ItemStack item) {
		setProduct(item);

	}

	public Product(String name, ItemStack icon) {
		super(name, icon);
		// TODO Auto-generated constructor stub
	}

	public Product(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public ItemStack getProduct() {
		return product;
	}

	public void setProduct(ItemStack product) {
		this.product = product;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public boolean isLimited() {
		return limited;
	}

	public void setLimited(boolean limited) {
		this.limited = limited;
	}

}
