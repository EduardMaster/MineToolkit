package net.eduard.api.server

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.database.StorageManager
import net.eduard.api.lib.database.StorageType
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.plugin.IPlugin
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

/**
 * Representa os Plugins meus feitos para o Bungeecord
 *
 * @author Eduard
 */
open class EduardBungeePlugin : Plugin(), IPlugin {


    fun registerEvents(events: Listener) {

        ProxyServer.getInstance().pluginManager.registerListener(this, events)
    }

    fun registerCommand(comand: Command) {
        ProxyServer.getInstance().pluginManager.registerCommand(this, comand)
    }
    override fun console(message: String) {
        ProxyServer.getInstance().console.sendMessage(TextComponent(message))
    }
    private var started = false

    override val pluginName: String
        get() = plugin.description.name

    val config get() = configs

    override fun getPlugin(): Plugin {
        return this
    }


    override var isActivated = false
    override var isFree: Boolean = false
    final override lateinit var configs: Config
    final override lateinit var messages: Config
    final override lateinit var storage: Config
    final override lateinit var databaseFile : File
    final override var dbManager: DBManager = DBManager()
    final override var sqlManager: SQLManager = SQLManager()
    final override var storageManager: StorageManager = StorageManager()
    private val prefix get() = "[$pluginName] "
    override fun log(message: String) {
        console("§b$prefix§a$message")
    }

    override fun error(message: String) {
        console("§e$prefix§c$message")
    }

    override fun onEnable() {
        if (!started) onLoad()
    }

    override fun onLoad() {
        started = true
        configs = Config(this, "config.yml")
        messages = Config(this, "messages.yml")
        storage = Config(this, "storage.yml")
        databaseFile = File(pluginFolder, "database.db")
        config.add("database-type", StorageType.YAML)
        config.add("log-enabled", true)
        configs.add("auto-save", false)
        configs.add("auto-save-seconds", 60)
        configs.add("auto-save-lasttime", Extra.getNow())
        configs.add("backup", false)
        configs.add("backup-lasttime", Extra.getNow())
        configs.add("backup-time", 1)
        configs.add("backup-timeunit-type", "MINUTES")
        configs.add("database", dbManager)

        configs.saveConfig()
        dbManager = config.get("database", DBManager::class.java)
        storageManager.type = config.get("database-type", StorageType::class.java)
        if (db.isEnabled) {
            db.openConnection()
            sqlManager.setDbManager(dbManager)
            storageManager.sqlManager = sqlManager
        }

        storageManager.folderBase = File(pluginFolder,"/database/")
        storageManager.folderBase.mkdirs()
    }
    /**
     * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
     */
    override fun backup() {
        configs.set("backup-lasttime", Extra.getNow())
        try {
            val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY HH-mm-ss")
            val pasta = File(pluginFolder,
                    "/backup/" + simpleDateFormat.format(System.currentTimeMillis()) + "/")

            pasta.mkdirs()

            if (storage.existConfig() && storage.keys.isNotEmpty()) {

                Files.copy(storage.file.toPath(), Paths.get(pasta.path, storage.name))
            }
            if (configs.existConfig() && storage.keys.isNotEmpty()) {

                Files.copy(configs.file.toPath(), Paths.get(pasta.path, configs.name))
            }
            if (databaseFile.exists()) {
                Files.copy(databaseFile.toPath(), Paths.get(pasta.path, databaseFile.name))
            }
        } catch (e: IOException) {

            e.printStackTrace()
        }

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





    /**
     * Deleta os backups dos dias anteriores
     */
    override fun deleteOldBackups() {
        val pasta = File(pluginFolder, "/backup/")
        pasta.mkdirs()
        val lista = listOf(*pasta.listFiles()!!)
        lista.filter { it.lastModified() + TimeUnit.DAYS.toMillis(1) <= System.currentTimeMillis() }
                .forEach {
                    Extra.deleteFolder(it)
                    if (it.exists())
                        it.delete()
                }
    }



}
