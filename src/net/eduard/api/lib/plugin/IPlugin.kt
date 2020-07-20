package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.StorageManager
import net.eduard.api.lib.database.api.SQLManager
import net.eduard.api.lib.storage.StorageAPI
import java.io.File
import java.util.concurrent.TimeUnit

interface IPlugin {
    var isActivated: Boolean
    var isFree: Boolean
    var configs: Config
    var storage: Config
    var messages: Config
    var databaseFile: File
    var dbManager: DBManager
    var sqlManager : SQLManager
    var storageManager : StorageManager



    fun deleteOldBackups()
    fun backup()
    fun getName(): String
    fun getDataFolder(): File
    fun log(message : String)
    fun error(message : String)

    fun onLoad()
    fun onEnable()
    fun onActivation() {}
    fun onDisable()

    fun save() {

    }

    fun reload() {

    }

    fun configDefault() {

    }

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