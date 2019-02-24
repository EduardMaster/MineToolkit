package net.eduard.api.lib.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ProductTradeEvent extends PlayerEvent implements Cancellable {
	private Product product;
	private Shop shop;
	private int amount;
	private int newStock;
	private TradeType type;
	private boolean cancelled;
	private double balance;
	private double priceTotal;

	public ProductTradeEvent(Player player) {
		super(player);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private static final HandlerList handlers = new HandlerList();

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public TradeType getType() {
		return type;
	}

	public void setType(TradeType type) {
		this.type = type;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;

	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getPriceTotal() {
		return priceTotal;
	}

	public void setPriceTotal(double priceTotal) {
		this.priceTotal = priceTotal;
	}

	public int getNewStock() {
		return newStock;
	}

	public void setNewStock(int newStock) {
		this.newStock = newStock;
	}

}
