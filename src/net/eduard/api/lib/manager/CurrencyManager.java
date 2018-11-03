package net.eduard.api.lib.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAttributes;

@StorageAttributes(indentificate = true)
public class CurrencyManager implements Storable {
	private String name = "Money";
	private String symbol = "$";
	private double inicialAmount;
	private Map<OfflinePlayer, Double> currency = new HashMap<>();

	public synchronized double getBalance(FakePlayer player) {

//		System.out.println("Resultado "+ (currency.get(player)));
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

	public void save(File file) {
		StringBuilder b = new StringBuilder();
		for (Entry<OfflinePlayer, Double> entry : currency.entrySet()) {
			b.append(entry.getKey().getName());
			b.append(";");
			b.append(entry.getKey().getUniqueId());
			b.append(";");
			b.append(entry.getValue());
			b.append("\n");
		}
		try {
			Files.write(file.toPath(), b.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void load(File file) {
		currency.clear();

		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String line : lines) {
				String[] split = line.split(";");
				try {
					String key = split[0];
					UUID uuid = UUID.fromString(split[1]);
					Double value = Double.valueOf(split[2]);
					FakePlayer fake = new FakePlayer(key, uuid);
					currency.put(fake, value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void setBalance(FakePlayer player, double amount) {

		currency.put(player, amount);

	}

	public void addBalance(FakePlayer player, double amount) {
		setBalance(player, getBalance(player) + amount);
	}

	public void removeBalance(FakePlayer player, double amount) {
		setBalance(player, getBalance(player) - amount);
	}

	public Map<OfflinePlayer, Double> getCurrency() {
		return currency;
	}

	public void setCurrency(Map<OfflinePlayer, Double> currency) {
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
