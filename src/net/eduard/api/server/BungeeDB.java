package net.eduard.api.server;

import java.util.UUID;

import net.eduard.api.lib.manager.DBManager;

public class BungeeDB {

	private DBManager manager;

	public BungeeDB(DBManager manager) {
		setManager(manager);
	}
	public void createBungeeTables() {
		getDB().createTable("servers",
				"name varchar(50), host varchar(100), port int, players int, status int");
		getDB().createTable("players", "name varchar(16), uuid varchar(100), server varchar(50)");
	}

	/**
	 * Adiciona um numero atual do tempo na tabela para o jogador
	 * 
	 * @param table
	 *            Tabela
	 * @param player
	 *            Jogador
	 */
	public void addCooldown(String table, String playerId) {
		getDB().createTable(table, "uuid varchar not null, time long not null");

		if (!getDB().contains(table, "uuid = ?", playerId)) {
			getDB().insert(table, playerId, System.currentTimeMillis());
		} else {
			getDB().change(table, "id = ?", "time = ?", playerId, System.currentTimeMillis());
		}
	}

	public void setStatusServer(String server, int status) {
		getDB().change("servers", "status = ?", "name = ?", status, server);
	}
	public void setPlayerServer(UUID playerId,String serverName) {
		getDB().change("players", "server = ?", "uuid = ?", serverName,
				playerId);
	}

	public boolean isOnServer(String server, String playerName) {
		return getDB().contains("players", "name = ? and server = ?", playerName, server);
	}

	public String getPlayerServer(UUID playerId) {
		return getDB().getString("players", "server", "uuid = ?", playerId);
	}

	public boolean playersContains(String name) {
		return getDB().contains("players", "name = ?", name);
	}

	public boolean playersContains(UUID playerId) {
		return getDB().contains("players", "uuid = ?", playerId);
	}

	public boolean serversContains(String name) {
		return getDB().contains("servers", "name = ?", name);
	}

	public void setPlayersAmount(String server, int amount) {
		getDB().change("servers", "players = ?", "name = ?", amount, server);
	}

	public int getPlayersAmount(String server) {
		return getDB().getInt("servers", "players", "name = ?", server);
	}

	public DBManager getDB() {
		return getManager();
	}

	public DBManager getManager() {
		return manager;
	}

	public void setManager(DBManager manager) {
		this.manager = manager;
	}

}
