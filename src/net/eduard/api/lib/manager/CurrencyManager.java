package net.eduard.api.lib.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;

public class CurrencyManager implements Storable {
	private String name = "Money";
	private String symbol = "$";
	private double inicialAmount;
	private Map<FakePlayer, Double> currency = new HashMap<>();

	
	public double getBalance(FakePlayer player) {
		
//		System.out.println("Resultado "+ (currency.get(player)));
		return currency.getOrDefault(player, inicialAmount);
	}
	public void save(File file) {
		StringBuilder b = new StringBuilder();
		for (Entry<FakePlayer, Double> entry : currency.entrySet()) {
			b.append(entry.getKey().getName());
			b.append(";");
			b.append(entry.getKey().getUniqueId());
			b.append(";");
			b.append(entry.getValue());
			b.append("\n");
		}
		try {
			Files.write(file.toPath(),b.toString().getBytes());
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

	public void setBalance(FakePlayer player, double amount) {

		currency.put(player, amount);

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
