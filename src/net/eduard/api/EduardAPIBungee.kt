package net.eduard.api

import net.eduard.api.lib.bungee.BungeeAPI
import net.eduard.api.lib.bungee.ServerState
import net.eduard.api.lib.plugin.HybridPlugin
import net.eduard.api.lib.plugin.IPluginInstance
import net.eduard.api.server.BungeeDB
import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.plugin.Plugin

/**
 * Para fazer plugins usando esta dependencia , lembre-se de colocar depends: [EduardAPI]
 * em vez de depend: [EduardAPI na bungee.yml
 * @author Eduard
 */
class EduardAPIBungee(plugin : IPluginInstance) : HybridPlugin(plugin) {
    lateinit var bungee: BungeeDB

    override fun getPlugin(): Plugin {
        return pluginBase.plugin as Plugin
    }

    override fun onEnable() {
        instance = this

        reload()

        val bungee = BungeeAPI.getBungee()
        bungee.plugin = plugin
        bungee.register()

       // BungeeCord.getInstance().getPluginManager().registerListener(this,BungeeEvents())
       // BungeeCord.getInstance().getPluginManager().registerCommand(this, BungeeReloadCommand())
    }

    override fun reload() {

        bungee = BungeeDB(db)
        if (db.isEnabled) {
            log("MySQL Ativado iniciando conexao")
            db.openConnection()
            if (db.hasConnection()) {
                bungee!!.createBungeeTables()
                for (server in BungeeCord.getInstance().servers.values) {
                    if (!bungee!!.serversContains(server.name)) {
                        db.insert("servers", server.name,
                                server.address.address.hostAddress, server.address.port, 0, 0)
                    } else {
                        db.change("servers", "host = ? , port = ?", "name = ?",
                                server.address.address.hostAddress, server.address.port,
                                server.name)
                    }
                }
            } else {
                error("§cFalha ao conectar com a Database")
            }
        } else {
            log("MySQL destivado algumas coisas da EduardBungeeAPI estarao desativado")
        }
        for (server in BungeeCord.getInstance().servers.values) {
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



    override fun onDisable() {
        db.closeConnection()
        BungeeAPI.getController().unregister()
    }





    companion object {
        lateinit var instance: EduardAPIBungee
    }
}