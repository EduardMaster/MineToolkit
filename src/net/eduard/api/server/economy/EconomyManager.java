package net.eduard.api.server.economy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {
	
	private Map<UUID, PlayerAccount> accounts = new HashMap<>();
	private Map<Integer, AccountTransaction> transactions = new HashMap<>();
	
	public Map<UUID, PlayerAccount> getAccounts() {
		return accounts;
	}
	public void setAccounts(Map<UUID, PlayerAccount> accounts) {
		this.accounts = accounts;
	}
	public Map<Integer, AccountTransaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(Map<Integer, AccountTransaction> transactions) {
		this.transactions = transactions;
	}
	
	
	
	
}
