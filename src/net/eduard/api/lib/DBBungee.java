package net.eduard.api.lib;

import java.util.UUID;

import net.eduard.api.lib.storage.manager.DBManager;

public class DBBungee extends DBManager {
	
	
	public DBBungee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DBBungee(String user, String pass, String host, String database) {
		super(user, pass, host, database);
		// TODO Auto-generated constructor stub
	}

	public DBBungee(String user, String pass, String host) {
		super(user, pass, host);
		// TODO Auto-generated constructor stub
	}

	public void createBungeeTables() {
		createTable("servers",
				"name varchar(50), host varchar(100), port int, players int, status int");
		createTable("players", "name varchar(16), uuid varchar(100), server varchar(50)");
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
		createTable(table, "uuid varchar not null, time long not null");

		if (!contains(table, "uuid = ?", playerId)) {
			insert(table, playerId, System.currentTimeMillis());
		} else {
			change(table, "id = ?", "time = ?", playerId, System.currentTimeMillis());
		}
	}

	public void setStatusServer(String server, int status) {
		change("servers", "status = ?", "name = ?", status, server);
	}
	public void setPlayerServer(UUID playerId,String serverName) {
		change("players", "server = ?", "uuid = ?", serverName,
				playerId);
	}

	public boolean isOnServer(String server, String playerName) {
		return contains("players", "name = ? and server = ?", playerName, server);
	}

	public String getPlayerServer(UUID playerId) {
		return getString("players", "server", "uuid = ?", playerId);
	}

	public boolean playersContains(String name) {
		return contains("players", "name = ?", name);
	}

	public boolean playersContains(UUID playerId) {
		return contains("players", "uuid = ?", playerId);
	}

	public boolean serversContains(String name) {
		return contains("servers", "name = ?", name);
	}

	public void setPlayersAmount(String server, int amount) {
		change("servers", "players = ?", "name = ?", amount, server);
	}

	public int getPlayersAmount(String server) {
		return getInt("servers", "players", "name = ?", server);
	}


}
