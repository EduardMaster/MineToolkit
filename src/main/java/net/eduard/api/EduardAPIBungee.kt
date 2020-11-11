package net.eduard.api

import net.eduard.api.command.bungee.BungeeReloadCommand
import net.eduard.api.lib.bungee.BungeeAPI
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.hybrid.BungeeServer
import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.listener.BungeeEvents
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File


class EduardAPIBungee(val plugin: Plugin) : IPlugin {
    companion object {
        lateinit var instance: EduardAPIBungee
    }
    init {
        instance = this
    }





    override var started = false
    override lateinit var configs: Config
    override lateinit var storage: Config
    override lateinit var messages: Config
    override lateinit var dbManager: DBManager
    override lateinit var sqlManager: SQLManager
    override lateinit var storageManager: StorageManager
    override val pluginName: String
        get() = plugin.description.name
    override val pluginFolder: File
        get() = plugin.dataFolder

    override fun log(message: String) {
        ProxyServer.getInstance().console.sendMessage("§f$message")
    }

    override fun error(message: String) {
        ProxyServer.getInstance().console.sendMessage("§c$message")
    }

    override fun reload() {
        if (dbManager.hasConnection()) {
            log("MySQL Ativado, iniciando conexao")

        }
    }

    override fun configDefault() {

    }

    override fun save() {

    }


    override fun onDisable() {
        BungeeAPI.getController().unregister()

    }

    override fun unregisterTasks() {

    }

    override fun unregisterListeners() {

    }

    override fun unregisterServices() {

    }

    override fun unregisterCommands() {

    }

    override fun getPlugin(): Any {
        return plugin
    }

    override fun onEnable() {
        super.onLoad()
        Hybrid.instance = BungeeServer

        reload()

        ProxyServer.getInstance().pluginManager
            .registerListener(plugin, BungeeEvents())
        ProxyServer.getInstance().pluginManager
            .registerCommand(plugin, BungeeReloadCommand())

    }


}