package net.eduard.api.lib.old;

import org.bukkit.entity.Player;

import net.eduard.api.lib.manager.CurrencyManager;

/**
 * Sistema de criar configurações que armanzena o dinheiro dos jogadores<br>
 * 
 * @deprecated Versão atual {@link CurrencyManager}<br>
 * Versão anterior {@link MoneySetup} 1.0
 * @version 2.0
 * @since 0.9
 * @author Eduard
 *
 */
public class Money {
	private Config1 config;
	private String name;

	public Money(String moneyName) {
		this(moneyName, 0);
	}

	public Money(String moneyName, int moneyStart) {
		setConfig(new Config1("Money/" + moneyName + ".yml"));
		setMoneyStart(moneyStart);
		setName(moneyName);
	}

	public Config1 getConfig() {
		return this.config;
	}

	public int getMoney(Player p) {
		if (!hasMoney(p)) {
			setMoney(p, getMoneyStart());
		}
		return this.config.getInt("Money." + p.getUniqueId() + ".value").intValue();
	}

	public int getMoneyStart() {
		return this.config.getInt("start-money").intValue();
	}

	public String getName() {
		return this.name;
	}

	public boolean hasMoney(Player p) {
		return this.config.has("Money." + p.getUniqueId() + ".value");
	}

	private void setConfig(Config1 config) {
		this.config = config;
	}

	public void setMoney(Player p, int money) {
		this.config.create("Money." + p.getUniqueId() + ".names." + p.getName());
		this.config.set("Money." + p.getUniqueId() + ".value", Integer.valueOf(money));
		this.config.saveConfig();
	}

	public void setMoneyStart(int value) {
		this.config.set("start-money", Integer.valueOf(value));
		this.config.saveConfig();
	}

	private void setName(String name) {
		this.name = name;
	}
}
