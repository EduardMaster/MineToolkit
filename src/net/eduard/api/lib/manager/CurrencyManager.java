package net.eduard.api.lib.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.eduard.api.lib.game.FakePlayer;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.server.currency.CurrencyHandler;
import org.bukkit.inventory.ItemStack;


public class CurrencyManager implements Storable , CurrencyHandler {
	private String name = "Money";
	private String symbol = "$";
	private double inicialAmount;
	private ItemStack icon;
	@StorageAttributes(inline = true)
	private Map<FakePlayer, Double> currency = new HashMap<>();
	
	

	public synchronized double getBalance(FakePlayer player) {
//		
//		System.out.println(currency);
		for (Entry<FakePlayer, Double> entry : getCurrency().entrySet()) {
			if (entry.getKey().equals(player)) {
				return entry.getValue();
			}
			
		}
	
		return inicialAmount;
//		return currency.getOrDefault(player, inicialAmount);
	}
	

	public CurrencyManager(String name, String symbol, double inicialAmount) {
		super();
		this.name = name;
		this.symbol = symbol;
		this.inicialAmount = inicialAmount;
	}

	public CurrencyManager() {
		// TODO Auto-generated constructor stub
	}



	public synchronized void setBalance(FakePlayer player, double amount) {
		for (Entry<FakePlayer, Double> entry : getCurrency().entrySet()) {
			if (entry.getKey().equals(player)) {
				entry.setValue(amount);
				return;
			}
		}
		currency.put(player, amount);

	}

	public boolean containsBalance(FakePlayer player, double amount) {
		return getBalance(player) >= amount;
	}

	public void addBalance(FakePlayer player, double amount) {
		setBalance(player, getBalance(player) + amount);
	}

	public void removeBalance(FakePlayer player, double amount) {
		setBalance(player, getBalance(player) - amount);
	}



	public synchronized Map<FakePlayer, Double> getCurrency() {
		return currency;
	}

	public void setCurrency(Map<FakePlayer, Double> currency) {
		this.currency = currency;
	}

	public String getName() {
		return name;
	}

	@Override
	public double get(FakePlayer player) {
		return getBalance(player);
	}

	@Override
	public ItemStack getIcon() {
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	@Override
	public boolean check(FakePlayer player, double amount) {
		return containsBalance(player,amount);
	}

	@Override
	public boolean remove(FakePlayer player, double amount) {
		removeBalance(player,amount);
		return true;
	}

	@Override
	public boolean add(FakePlayer player, double amount) {
		addBalance(player,amount);
		return true;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getInicialAmount() {
		return inicialAmount;
	}

	public void setInicialAmount(double inicialAmount) {
		this.inicialAmount = inicialAmount;
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}
}
