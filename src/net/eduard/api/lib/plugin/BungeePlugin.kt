package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.StorageManager
import net.eduard.api.lib.database.api.SQLManager
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

abstract class BungeePlugin : Plugin(), IPlugin {

    abstract val hybridPlugin: HybridPlugin
    val config get() = configs
    override fun onLoad() {

        hybridPlugin.onLoad()

    }

    override fun getName(): String {
        return description.name
    }

    override fun getPlugin(): Plugin {
        return this
    }


    override fun onEnable() {
        hybridPlugin.onEnable()
    }

    override fun onDisable() {
        hybridPlugin.onDisable()
    }

    override var isActivated: Boolean
        get() = hybridPlugin.isActivated
        set(value) {
            hybridPlugin.isActivated = value
        }

    override var isFree: Boolean
        get() = hybridPlugin.isFree
        set(value) {
            hybridPlugin.isFree = value
        }
    override var sqlManager: SQLManager
        get() = hybridPlugin.sqlManager
        set(value) {
            hybridPlugin.sqlManager = value
        }

    override var configs: Config
        get() = hybridPlugin.configs
        set(value) {
            hybridPlugin.configs = value
        }

    override fun backup() {
        hybridPlugin.backup()
    }

    override fun deleteOldBackups() {
        hybridPlugin.deleteOldBackups()
    }

    override var storageManager: StorageManager
        get() = hybridPlugin.storageManager
        set(value) {
            hybridPlugin.storageManager = value
        }

    override var messages: Config
        get() = hybridPlugin.configs
        set(value) {
            hybridPlugin.configs = value
        }
    override var storage: Config
        get() = hybridPlugin.storage
        set(value) {
            hybridPlugin.storage = value
        }

    override fun save() {
        hybridPlugin.save()
    }

    override fun configDefault() {
        hybridPlugin.configDefault()
    }

    override fun reload() {
        hybridPlugin.reload()
    }

    override fun unregisterServices() {
        hybridPlugin.unregisterServices()

    }

    override fun unregisterCommands() {
        hybridPlugin.unregisterCommands()

    }

    override fun unregisterListeners() {
        hybridPlugin.unregisterListeners()
    }

    override fun unregisterTasks() {
        hybridPlugin.unregisterTasks()
    }

    override fun unregisterStorableClasses() {
        hybridPlugin.unregisterStorableClasses()
    }

    override var databaseFile: File
        get() = hybridPlugin.databaseFile
        set(value) {
            hybridPlugin.databaseFile = value

        }
    override var dbManager: DBManager
        get() = hybridPlugin.dbManager
        set(value) {
            hybridPlugin.dbManager = value
        }

    override fun error(message: String) {
        hybridPlugin.error(message)
    }

    override fun log(message: String) {
        hybridPlugin.log(message)
    }

}