package net.eduard.api.server;

import net.eduard.api.lib.modules.FakePlayer;

public interface SoulSystem {
	
	public void addSouls(FakePlayer player,double amount);
	
	public void removeSouls(FakePlayer player,double amount);
	
	public double getSouls(FakePlayer player);
	
	public void setSouls(FakePlayer player,double amount);

}
