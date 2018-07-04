package net.eduard.api.server.economy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
	
	private Map<UUID, EconomyPlayer> accounts = new HashMap<>();
	private Map<Integer, EconomyTransaction> transactions = new HashMap<>();
	
	public Map<UUID, EconomyPlayer> getAccounts() {
		return accounts;
	}
	public void setAccounts(Map<UUID, EconomyPlayer> accounts) {
		this.accounts = accounts;
	}
	public Map<Integer, EconomyTransaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(Map<Integer, EconomyTransaction> transactions) {
		this.transactions = transactions;
	}
	
	
	
	
}
