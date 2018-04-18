package net.eduard.api.server.economy;

import java.util.UUID;

public class PlayerAccount  {

	private double balance;
	
	private UUID playerId;

	public UUID getPlayerId() {
		return playerId;
	}

	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
