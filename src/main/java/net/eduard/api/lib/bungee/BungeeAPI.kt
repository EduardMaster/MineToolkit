package net.eduard.api.lib.bungee

import net.eduard.api.lib.hybrid.Hybrid
import java.lang.Exception

object BungeeAPI {

    fun debug(msg : String){

        Hybrid.instance.console.sendMessage("§b[BungeeAPI] §f$msg")

    }

    const val channel = "bukkit:bungee"
    val servers = mutableMapOf<String, ServerSpigot>()
    val handlers = mutableListOf<ServerMessageHandler>()


    fun getServer(serverName: String): ServerSpigot {
        return servers.getOrPut(serverName.toLowerCase() , {
            ServerSpigot(serverName)
        })
    }

    val playersAmount: Int
        get() {
            var amount = 0
            for (server in servers.values) {
                amount += server.count
            }
            return amount
        }

    fun getPlayersAmount(serverType: String): Int {
        var amount = 0
        for (server in servers.values) {
            if (server.type.equals(serverType, ignoreCase = true)) {
                amount += server.count
            }
        }
        return amount
    }

    fun getPlayersAmount(serverType: String, teamSize: Int): Int {

        var amount = 0
        for (server in servers.values) {
            if (!server.subType.equals("lobby", ignoreCase = true)
                && server.type.equals(serverType, ignoreCase = true)
                && server.teamSize == teamSize
            ) {
                amount += server.count
            }
        }

        return amount
    }

    fun getPlayersAmount(serverType: String, subType: String): Int {
        var amount = 0
        for (server in servers.values) {
            if (server.type.equals(serverType, ignoreCase = true) &&
                subType.equals(server.subType, ignoreCase = true)
            ) {
                amount += server.count
            }
        }
        return amount
    }

    val bukkit: BukkitController
        get() = controller as BukkitController
    val bungee: BungeeController
        get() = controller as BungeeController

    fun register(receiver: ServerMessageHandler) {
        handlers.add(receiver)
    }
    var controller: ServerController<*> = try {
        Class.forName("org.bukkit.Bukkit")
        BukkitController()
    } catch (e: Exception) {
        BungeeController()
    }

}