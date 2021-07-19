package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.HybridTypes
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.storage.StorageAPI
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

interface IPlugin : IPluginInstance {
    var started: Boolean
    var configs: Config
    var storage: Config
    var messages: Config
    var dbManager: DBManager
    var sqlManager: SQLManager
    var storageManager: StorageManager
    var settings: PluginSettings

    fun onLoad() {
        HybridTypes
        if (started) return
        dbManager = DBManager()
        configs = Config(this, "config.yml")
        messages = Config(this, "messages.yml")
        storage = Config(this, "storage.yml")
        settings = PluginSettings()
        configs.add("settings", settings)
        configs.add("database", dbManager)
        configs.saveConfig()
        settings =  configs.get("settings", PluginSettings::class.java)
        dbManager = configs.get("database", DBManager::class.java)
        sqlManager = SQLManager(dbManager)
        storageManager = StorageManager(sqlManager)
        started = true
        storageManager.type = settings.storeType
        if (db.isEnabled) {
            db.openConnection()
        }
    }


    /**
     * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
     */
    fun backup() {
        settings.lastBackup = Extra.getNow()

        try {
            val simpleDateFormat = SimpleDateFormat("dd-MM-YYYY HH-mm-ss")
            val pasta = File(
                pluginFolder,
                "/backup/" + simpleDateFormat.format(System.currentTimeMillis()) + "/"
            )
            pasta.mkdirs()
            if (storage.existConfig() && storage.keys.isNotEmpty()) {
                Files.copy(storage.file.toPath(), Paths.get(pasta.path, storage.name))
            }
            if (configs.existConfig() && storage.keys.isNotEmpty()) {
                Files.copy(configs.file.toPath(), Paths.get(pasta.path, configs.name))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun unregisterStorableClasses() {
        StorageAPI.unregisterPlugin(javaClass)
    }

    override fun getSystemName(): String {
        return pluginName
    }
    val pluginName: String
    val pluginFolder: File
    fun log(message: String)
    fun error(message: String)
    fun console(message: String) {
        println(message)
    }

    fun onEnable()
    fun reload()
    fun configDefault()
    fun save()
    fun onActivation() {}
    fun onDisable()
    fun unregisterTasks()
    fun unregisterListeners()
    fun unregisterServices()
    fun unregisterCommands()
    var db
        get() = dbManager
        set(data) {
            dbManager = data
        }


    fun getBoolean(path: String): Boolean {
        return configs.getBoolean(path)
    }

    fun getInt(path: String): Int {
        return configs.getInt(path)
    }

    fun getDouble(path: String): Double {
        return configs.getDouble(path)
    }

    fun message(path: String): String {
        return messages.message(path)
    }

    fun getMessages(path: String): List<String> {
        return messages.getMessages(path)
    }

    fun getString(path: String): String {

        return configs.message(path)
    }

    fun disconnectDB() {
        if (db.hasConnection()) {
            db.closeConnection()
        }
    }

    /**
     * Deleta os backups dos dias anteriores
     */
    fun deleteOldBackups() {
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