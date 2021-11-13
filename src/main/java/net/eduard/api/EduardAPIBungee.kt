package net.eduard.api

import net.eduard.api.command.bungee.BungeeReloadCommand
import net.eduard.api.lib.bungee.BungeeAPI
import net.eduard.api.lib.bungee.ServerSpigot
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.HybridTypes
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.hybrid.BungeeServer
import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.plugin.IPluginInstance
import net.eduard.api.lib.plugin.PluginSettings
import net.eduard.api.lib.storage.StorageAPI
import net.eduard.api.lib.storage.storables.BukkitStorables
import net.eduard.api.listener.BungeePlugins
import net.eduard.api.task.BungeeDatabaseUpdater
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.concurrent.TimeUnit


@Suppress("deprecated")
class EduardAPIBungee(val plugin: Plugin): IPluginInstance {

    companion object {
        lateinit var instance: EduardAPIBungee

        init {
            Hybrid.instance = BungeeServer
        }
    }

    init {
        instance = this
    }


    var started = false
    lateinit var configs: Config
    lateinit var storage: Config
    lateinit var messages: Config
    lateinit var dbManager: DBManager
    lateinit var sqlManager: SQLManager
    lateinit var settings: PluginSettings

    val pluginName: String
        get() = plugin.description.name

    val dataFolder get() = plugin.dataFolder

    override fun getPluginFolder(): File {
        return  plugin.dataFolder
    }
    fun log(message: String) {
        console("§f$message")
    }

    fun console(message: String) {
        ProxyServer.getInstance().console
            .sendMessage(TextComponent("§b[EduardAPI]§r $message"))
    }

    fun error(message: String) {
        console("§c$message")
    }

    fun onLoad() {
        HybridTypes
        StorageAPI.setDebug(false)
        val currentInstance: EduardAPIBungee = this
        if (!currentInstance.started) {
            currentInstance.dbManager = DBManager()
            currentInstance.configs = Config(currentInstance, "config.yml")
            currentInstance.messages = Config(currentInstance, "messages.yml")
            currentInstance.storage = Config(currentInstance, "storage.yml")
            currentInstance.settings = PluginSettings()
            currentInstance.configs.add("settings", currentInstance.settings)
            currentInstance.configs.add("database", currentInstance.dbManager)
            currentInstance.configs.saveConfig()
            currentInstance.settings = currentInstance.configs.get("settings", PluginSettings::class.java)
            currentInstance.dbManager = currentInstance.configs.get("database", DBManager::class.java)
            currentInstance.sqlManager = SQLManager(currentInstance.dbManager)
            //  currentInstance.setStorageManager(new StorageManager(currentInstance.getSqlManager()));
            currentInstance.started = true
            // currentInstance.getStorageManager().setType(currentInstance.getSettings().getStoreType());
            if (currentInstance.dbManager.isEnabled) {
                currentInstance.dbManager.openConnection()
            }
        }


    }

    fun getBoolean(key: String) = configs.getBoolean(key)
    fun reload() {
        log("Inicio do Recarregamento do EduardAPI")
        configs.reloadConfig()
        messages.reloadConfig()
        if (getBoolean("bungee-api")) {
            BungeeAPI.bungee.register(plugin)
        }
        configDefault()
        log("Ativando debug de sistemas caso marcado na config como 'true'")
        StorageAPI.setDebug(configs.getBoolean("debug.storage"))
        DBManager.setDebug(configs.getBoolean("debug.database"))
        Copyable.setDebug(configs.getBoolean("debug.copyable"))

        try {
            log("Carregando formato de dinheiro da config")
            Extra.MONEY = DecimalFormat(
                configs.getString("money-format"),
                DecimalFormatSymbols.getInstance(Locale.forLanguageTag(configs.getString("money-format-locale")))
            )
            log("Formato valido")
        } catch (exception: Exception) {
            error("Formato do dinheiro invalido " + configs.getString("money-format"))
        }

        mysqlDownload()
        log("Recarregamento do EduardAPI concluido.")
    }

    fun mysqlDownload() {

        if (!dbManager.hasConnection()) return
        log("SQL Conectado iniciando modifications")
        sqlManager.createTable(ServerSpigot::class.java)

        if (!getBoolean("bungee-api")) return
        for (server in sqlManager.getAll<ServerSpigot>()) {
            BungeeAPI.servers[server.name.toLowerCase()] = server
        }
        for (server in ProxyServer.getInstance().servers.values) {
            val spigot = BungeeAPI.servers[server.name.toLowerCase()]
            if (spigot == null) {
                val servidor = BungeeAPI.getServer(server.name)
                servidor.host = server.address.hostName
                servidor.port = server.address.port
                servidor.players = server.players
                    .map { it.name }
                servidor.count = server.players.size
                sqlManager.insertData(servidor)
            }
        }
    }

    fun onEnable() {
        StorageAPI.setDebug(false)

        reload()

        ProxyServer.getInstance().pluginManager
            .registerCommand(plugin, BungeeReloadCommand())

        ProxyServer.getInstance().scheduler.schedule(
            plugin,
            BungeeDatabaseUpdater(),
            1, 1, TimeUnit.SECONDS
        );

        ProxyServer.getInstance().scheduler.schedule(
            plugin, BungeePlugins(),
            1, 1, TimeUnit.SECONDS
        );


    }

    fun configDefault() {
        configs.add("bungee-api", true)
        configs.add("debug.storage", false)
        configs.add("debug.copyable", false)
        configs.add("debug.commands", false)
        configs.add("debug.replacers", false)
        configs.add("debug.database", false)
        configs.add("money-format", "###,###.##")
        configs.add("money-format-locale", "PT-BR")
        configs.saveConfig()

    }

    fun save() {

    }


    fun onDisable() {
        if (getBoolean("bungee-api")) {
            BungeeAPI.bungee.unregister()
        }
        dbManager.closeConnection()


    }

    fun unregisterTasks() {

    }

    fun unregisterListeners() {

    }

    fun unregisterServices() {

    }

    fun unregisterCommands() {

    }

    override fun getPlugin(): Any {
        return plugin
    }

    override fun getSystemName(): String {
        return pluginName;
    }



}