package net.eduard.api.server

import java.util.UUID

import net.eduard.api.lib.database.DBManager

class BungeeDB(var db: DBManager) {

    fun createBungeeTables() {
        db.createTable("servers",
                "name varchar(50), host varchar(100), port int, players int, status int")
        db.createTable("players", "name varchar(16), uuid varchar(100), server varchar(50)")
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

        val data = db.select("select * from players where uuid = ?",playerId)
        var server = "lobby"
        if (data.next()){
            server = data.getString("server")
        }
        data.close();

        return  server;
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

        val data = db.select("select * from servers where name = ?",server)
        var amount = 0
        if (data.next()){
            amount = data.getInt("players")
        }
        data.close();

        return  amount;
    }

}
