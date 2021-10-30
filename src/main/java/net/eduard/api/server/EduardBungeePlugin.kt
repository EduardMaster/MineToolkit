package net.eduard.api.server

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.kotlin.resolvePut
import net.eduard.api.lib.kotlin.resolveTake
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.plugin.PluginSettings
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

/**
 * Representa os Plugins meus feitos para o Bungeecord
 *
 * @author Eduard
 */
open class EduardBungeePlugin : Plugin(), IPlugin {

    override var started = false
    var isFree= false



    fun registerEvents(events: Listener) {

        ProxyServer.getInstance().pluginManager.registerListener(this, events)
    }

    fun registerCommand(comand: Command) {
        ProxyServer.getInstance().pluginManager.registerCommand(this, comand)
    }
    override fun console(message: String) {
        ProxyServer.getInstance().console.sendMessage(TextComponent(message))
    }

    override val pluginName: String
        get() = plugin.description.name
    override val pluginFolder: File
        get() = plugin.dataFolder

    val config get() = configs

    override fun getPlugin(): Plugin {
        return this
    }


    final override lateinit var configs: Config
    final override lateinit var messages: Config
    final override lateinit var storage: Config
    final override  var dbManager : DBManager = DBManager()
    final override lateinit var sqlManager: SQLManager
    final override lateinit var storageManager: StorageManager
    final override lateinit var settings: PluginSettings
    private val prefix get() = "[$pluginName] "
    override fun log(message: String) {
        console("§b$prefix§a$message")
    }

    override fun error(message: String) {
        console("§e$prefix§c$message")
    }
    override fun onLoad() {
        super<IPlugin>.onLoad()
    }
    override fun onEnable() {
        if (!started) onLoad()
        resolvePut(this)
    }
    override fun onDisable() {
        resolveTake(this)
    }
    override fun save() {

    }

    override fun reload() {

    }

    override fun configDefault() {

    }
    override fun unregisterTasks() {

    }

    override fun unregisterServices() {

    }

    override fun unregisterListeners() {

    }

    override fun unregisterCommands() {

    }

    override fun unregisterStorableClasses() {

    }






}
