package net.eduard.api.lib.old;

import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.manager.CurrencyManager;

/**
 * Sistema de criar configurações que armanzena o dinheiro dos jogadores<br>
 * 
 * 
 * @deprecated Versão atual {@link CurrencyManager}<br>
 *             Versão nova {@link Money} 2.0
 * 
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class MoneySetup extends ConfigSetup {
	public MoneySetup(JavaPlugin plugin, String moneyName, int moneyStart) {
		super(plugin, "Coin/" + moneyName + ".yml");
		set("start-money", Integer.valueOf(moneyStart));
		saveConfig();
	}

	public MoneySetup(JavaPlugin plugin, String moneyName) {
		this(plugin, moneyName, 0);
	}

	public void setMoney(String player, int money) {
		set(player, Integer.valueOf(money));
		saveConfig();
	}

	public int getMoney(String player) {
		if (!has(player))
			setMoney(player, ((Integer) get("start-money")).intValue());
		return ((Integer) get(player)).intValue();
	}

	public boolean hasMoney(String player) {
		return has(player);
	}
}
