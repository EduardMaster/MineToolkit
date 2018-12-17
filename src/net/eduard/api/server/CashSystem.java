package net.eduard.api.server;

import net.eduard.api.lib.modules.FakePlayer;

public interface CashSystem {

	public void addCash(FakePlayer player, double amount);

	public void removeCash(FakePlayer player, double amount);

	public double getCash(FakePlayer player);

	public void setCash(FakePlayer player, double amount);

	public default boolean hasCash(FakePlayer player, double amount) {
		return getCash(player) >= amount;
	}

}
