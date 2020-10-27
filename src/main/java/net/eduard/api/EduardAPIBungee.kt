package net.eduard.api

import net.eduard.api.command.bungee.BungeeReloadCommand
import net.eduard.api.lib.bungee.BungeeAPI
import net.eduard.api.lib.bungee.ServerState
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.listener.BungeeEvents
import net.eduard.api.server.BungeeDB
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin


class EduardAPIBungee(val plugin: Plugin) {

    init {
        instance = this
    }

    companion object {

        lateinit var instance: EduardAPIBungee
    }

    lateinit var databaseManager: DBManager
    lateinit var config: Config
    lateinit var bungeeDB: BungeeDB
    fun onEnable() {
        config = Config(plugin, "config.yml")
        databaseManager = DBManager()
        reload()

        ProxyServer.getInstance().pluginManager
            .registerListener(plugin, BungeeEvents())
        ProxyServer.getInstance().pluginManager
            .registerCommand(plugin, BungeeReloadCommand())
    }

    fun log(msg: String) {
        ProxyServer.getInstance().console.sendMessage("§f" + msg)
    }

    fun error(msg: String) {
        ProxyServer.getInstance().console.sendMessage("§c" + msg)
    }

    fun reload() {

        bungeeDB = BungeeDB(databaseManager)

        if (databaseManager.isEnabled) {
            log("MySQL Ativado, iniciando conexao")
            databaseManager.openConnection()
            if (databaseManager.hasConnection()) {
                bungeeDB.createNetworkTables()
                for (server in ProxyServer.getInstance().servers.values) {
                    if (!bungeeDB.serversContains(server.name)) {
                        databaseManager.insert(
                            bungeeDB.serverTable, server.name,
                            server.address.address.hostAddress, server.address.port, 0, 0
                        )
                    } else {
                        databaseManager.change(
                            bungeeDB.serverTable, "host = ? , port = ?", "name = ?",
                            server.address.address.hostAddress, server.address.port,
                            server.name
                        )
                    }
                }
            } else {
                error("Falha ao conectar com ao MySQL")
            }
        } else {
            error("MySQL desativado algumas coisas da EduardBungeeAPI estarao desativado")
        }
        for (server in ProxyServer.getInstance().servers.values) {
            config.add("servers." + server.name + ".enabled", true)
            config.add("servers." + server.name + ".type", 0)
        }
        config.saveConfig()
        val bungee = BungeeAPI.getBungee()
        bungee.plugin = plugin
        bungee.register()
        for (serverName in config.getSection("servers").keys) {
            val enabled = config.getBoolean("servers.$serverName.enabled")
            val type = config.getInt("servers.$serverName.type")
            val server = BungeeAPI.getServer(serverName)
            server.type = type
            if (enabled) {
                server.setState(ServerState.OFFLINE)
            } else {
                server.setState(ServerState.DISABLED)
            }
        }
    }


    fun onDisable() {
        databaseManager.closeConnection()
        BungeeAPI.getController().unregister()
    }


}