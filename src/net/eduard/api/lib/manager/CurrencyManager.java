package net.eduard.api.lib.manager;

import java.util.HashMap;
import java.util.Map;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAttributes;

@StorageAttributes(indentificate = true)
public class CurrencyManager implements Storable {
	private String name = "Money";
	private String symbol = "$";
	private double inicialAmount;
	private Map<FakePlayer, Double> currency = new HashMap<>();

	public synchronized double getBalance(FakePlayer player) {

		return currency.getOrDefault(player, inicialAmount);
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



	public Map<FakePlayer, Double> getCurrency() {
		return currency;
	}

	public void setCurrency(Map<FakePlayer, Double> currency) {
		this.currency = currency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
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

}
