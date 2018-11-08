package net.eduard.api.server;

import net.eduard.api.lib.modules.FakePlayer;

public interface VipSystem {

	public void addVip(FakePlayer player, long millis, String vip);

	public void removeVip(FakePlayer player, String vip);

	public boolean hasVip(FakePlayer player);

	public boolean hasVip(FakePlayer player, String vip);
}
