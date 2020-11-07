package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.config.StorageType
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.storage.StorageAPI
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

interface IPlugin : IPluginInstance {
    var started : Boolean
    var configs: Config
    var storage: Config
    var messages: Config
    var dbManager: DBManager
    var sqlManager : SQLManager
    var storageManager : StorageManager

    fun onLoad() {

        configs = Config(this, "config.yml")
        messages = Config(this, "messages.yml")
        storage = Config(this, "storage.yml")
        configs.add("database-type", StorageType.YAML)
        configs.add("log-enabled", true)
        configs.add("auto-save", false)
        configs.add("auto-save-seconds", 60)
        configs.add("auto-save-lasttime", Extra.getNow())
        configs.add("backup", false)
        configs.add("backup-lasttime", Extra.getNow())
        configs.add("backup-time", 1)
        configs.add("backup-timeunit-type", "MINUTES")
        configs.add("database", dbManager)

        configs.saveConfig()
        dbManager = configs.get("database", DBManager::class.java)

        sqlManager = SQLManager(dbManager)
        storageManager = StorageManager(sqlManager)
        storageManager.type = configs.get("database-type", StorageType::class.java)
        if (db.isEnabled) {
            db.openConnection()
        }


    }




    /**
     * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
     */
    fun backup() {
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
        } catch (e: IOException) {

            e.printStackTrace()
        }
    }



     fun unregisterStorableClasses() {
        StorageAPI.debug("- CLASSES FROM PLUGIN $pluginName")
        val it = StorageAPI.getStorages().keys.iterator()
        var amount = 0
        while (it.hasNext()) {
            val next = it.next()
            val loader = next.classLoader
            if (loader != null) {
                if (loader == javaClass.classLoader) {
                    StorageAPI.getAliases().remove(next)
                    amount++
                    it.remove()
                }
            }
        }
        StorageAPI.debug("- CLASSES WITH SAME LOADER OF $pluginName : $amount")
    }


    val pluginName : String
    val pluginFolder get() : File{
        return try {
          Extra.getMethodInvoke(plugin, "getDataFolder") as File
        }catch (ex: Exception){
            File("plugins", pluginName)
        }
    }
    fun log(message : String)
    fun error(message : String)
    fun console(message : String){
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

    val isLogEnabled : Boolean
        get() = configs.getBoolean("log-enabled")


    val autoSaveSeconds: Long
        get() = configs.getLong("auto-save-seconds")!!

    val isAutoSaving: Boolean
        get() = configs.getBoolean("auto-save")

    val backupTime: Long
        get() = configs.getLong("backup-time")!!

    val backupLastTime: Long
        get() = configs.getLong("backup-lasttime")

    val autoSaveLastTime: Long
        get() = configs.getLong("auto-save-lasttime")

    val backupTimeUnitType: TimeUnit
        get() = TimeUnit.valueOf(configs.getString("backup-timeunit-type").toUpperCase())

    val canBackup: Boolean
        get() = configs.getBoolean("backup")
    fun disconnectDB() {
        db.closeConnection()
    }
    fun registerPackage(packname: String) {
        StorageAPI.registerPackage(javaClass, packname)
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