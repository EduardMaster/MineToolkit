package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.StorageManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.storage.StorageAPI
import java.io.File
import java.lang.Exception
import java.util.concurrent.TimeUnit

interface IPlugin  : IPluginInstance{

    var isActivated: Boolean
    var isFree: Boolean
    var configs: Config
    var storage: Config
    var messages: Config
    var databaseFile: File
    var dbManager: DBManager
    var sqlManager : SQLManager
    var storageManager : StorageManager








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
    fun deleteOldBackups()
    fun backup()
    fun onLoad()
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
    fun unregisterStorableClasses()
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
}