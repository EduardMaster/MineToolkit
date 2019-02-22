package net.eduard.api.server;

import net.eduard.api.lib.modules.FakePlayer;

public interface CoinSystem {

	public void addCoins(FakePlayer player, double amount);

	public void removeCoins(FakePlayer player, double amount);

	public double getCoins(FakePlayer player);

	public void setCoins(FakePlayer player, double amount);

	public default boolean hasCoins(FakePlayer player, double amount) {
		return getCoins(player) >= amount;
	}

}
