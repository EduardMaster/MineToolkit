package net.eduard.api.server

import java.util.UUID

import net.eduard.api.lib.database.DBManager

@SuppressWarnings("unused")
class BungeeDB(var db: DBManager) {

    val serverTable = "network_servers"
    val playerTable = "network_players"

    fun createNetworkTables() {
        db.createTable(serverTable,
                "name varchar(50), host varchar(100), port int, players int, status int")
        db.createTable(playerTable, "name varchar(16), uuid varchar(100), server varchar(50)")
    }

    fun setStatusServer(server: String, status: Int) {
        db.change(serverTable, "status = ?", "name = ?", status, server)
    }

    fun setPlayerServer(playerId: UUID, serverName: String) {
        db.change(serverTable, "server = ?", "uuid = ?", serverName,
                playerId)
    }

    fun isOnServer(server: String, playerName: String): Boolean {
        return db.contains(serverTable, "name = ? and server = ?", playerName, server)
    }

    fun getPlayerServer(playerId: UUID): String {

        val data = db.select("select * from $playerTable where uuid = ?",playerId)
        var server = "lobby"
        if (data.next()){
            server = data.getString("server")
        }
        data.close();

        return  server;
    }

    fun playersContains(name: String): Boolean {
        return db.contains(playerTable, "name = ?", name)
    }

    fun playersContains(playerId: UUID): Boolean {
        return db.contains(playerTable, "uuid = ?", playerId)
    }

    fun serversContains(name: String): Boolean {
        return db.contains(serverTable, "name = ?", name)
    }

    fun setPlayersAmount(server: String, amount: Int) {
        db.change(serverTable, "players = ?", "name = ?", amount, server)
    }

    fun getPlayersAmount(server: String): Int {

        val data = db.select("select * from $serverTable where name = ?",server)
        var amount = 0
        if (data.next()){
            amount = data.getInt("players")
        }
        data.close();

        return  amount;
    }

}
