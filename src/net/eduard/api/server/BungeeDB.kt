package net.eduard.api.server

import java.util.UUID

import net.eduard.api.lib.database.DBManager

class BungeeDB(manager: DBManager) {

    lateinit var db: DBManager



    fun createBungeeTables() {
        db.createTable("servers",
                "name varchar(50), host varchar(100), port int, players int, status int")
        db.createTable("players", "name varchar(16), uuid varchar(100), server varchar(50)")
    }

    /**
     * Adiciona um numero atual do tempo na tabela para o jogador
     *
     * @param table
     * Tabela
     * @param playerId
     * Jogador UUID
     */
    fun addCooldown(table: String, playerId: String) {
        db.createTable(table, "uuid varchar not null, time long not null")

        if (!db.contains(table, "uuid = ?", playerId)) {
            db.insert(table, playerId, System.currentTimeMillis())
        } else {
            db.change(table, "id = ?", "time = ?", playerId, System.currentTimeMillis())
        }
    }

    fun setStatusServer(server: String, status: Int) {
        db.change("servers", "status = ?", "name = ?", status, server)
    }

    fun setPlayerServer(playerId: UUID, serverName: String) {
        db.change("players", "server = ?", "uuid = ?", serverName,
                playerId)
    }

    fun isOnServer(server: String, playerName: String): Boolean {
        return db.contains("players", "name = ? and server = ?", playerName, server)
    }

    fun getPlayerServer(playerId: UUID): String {
        return db.getString("players", "server", "uuid = ?", playerId)
    }

    fun playersContains(name: String): Boolean {
        return db.contains("players", "name = ?", name)
    }

    fun playersContains(playerId: UUID): Boolean {
        return db.contains("players", "uuid = ?", playerId)
    }

    fun serversContains(name: String): Boolean {
        return db.contains("servers", "name = ?", name)
    }

    fun setPlayersAmount(server: String, amount: Int) {
        db.change("servers", "players = ?", "name = ?", amount, server)
    }

    fun getPlayersAmount(server: String): Int {
        return db.getInt("servers", "players", "name = ?", server)
    }

}
