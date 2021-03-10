package net.eduard.api

import net.eduard.api.command.bungee.BungeeReloadCommand
import net.eduard.api.lib.bungee.BungeeAPI
import net.eduard.api.lib.bungee.ServerSpigot
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.hybrid.BungeeServer
import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.modules.*
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.plugin.PluginSettings
import net.eduard.api.lib.storage.StorageAPI
import net.eduard.api.listener.BungeeListener
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


@Suppress("deprecated")
class EduardAPIBungee(val plugin: Plugin) : IPlugin {
    companion object {
        lateinit var instance: EduardAPIBungee
        init{
            Hybrid.instance = BungeeServer
        }
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
    override lateinit var settings: PluginSettings
    override val pluginName: String
        get() = plugin.description.name
    override val pluginFolder: File
        get() = plugin.dataFolder

    override fun log(message: String) {
        console("§f$message")
    }

    override fun console(message: String) {
        ProxyServer.getInstance().console.sendMessage("§b[EduardAPI]§r $message")
    }

    override fun error(message: String) {
       console("§c$message")
    }

    override fun reload() {
        log("Inicio do Recarregamento do EduardAPI")
        configs.reloadConfig()
        messages.reloadConfig()
        configDefault()
        log("Ativando debug de sistemas caso marcado na config como 'true'")
        StorageAPI.setDebug(configs.getBoolean("debug-storage"))
        DBManager.setDebug(configs.getBoolean("debug-database"))

        //configs.getBoolean("debug-commands")
        Copyable.CopyDebug.setDebug(configs.getBoolean("debug-copyable"))


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

        log("Recarregamento do EduardAPI concluido.")

        if (dbManager.hasConnection()) {
            log("MySQL Ativado, iniciando conexao")
            sqlManager.createTable(ServerSpigot::class.java)

            for (server in sqlManager.getAllData(ServerSpigot::class.java)) {
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
    }

    override fun onEnable() {

        super.onLoad()

        BungeeAPI.bungee.register(plugin)
        reload()

        ProxyServer.getInstance().pluginManager
            .registerListener(plugin, BungeeListener())
        ProxyServer.getInstance().pluginManager
            .registerCommand(plugin, BungeeReloadCommand())


        /*
        ProxyServer.getInstance().scheduler.schedule(plugin, {
            for (server in ProxyServer.getInstance().servers.values) {
                val spigot = BungeeAPI.getServers()[server.name.toLowerCase()]?:continue
                sqlManager.updateData(spigot)
            }
        },
        1,1,TimeUnit.SECONDS)

        */





    }

    override fun configDefault() {
        configs.add("debug-storage", false)
        configs.add("debug-copyable", false)
        configs.add("debug-commands", false)
        configs.add("debug-replacers", false)
        configs.add("debug-database", false)

        configs.add("money-format", "###,###.##")
        configs.add("money-format-locale", "PT-BR")

        configs.saveConfig()


    }

    override fun save() {

    }


    override fun onDisable() {
        BungeeAPI.controller.unregister()

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


}