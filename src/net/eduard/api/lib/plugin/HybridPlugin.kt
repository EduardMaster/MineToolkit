package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.DBType
import net.eduard.api.lib.database.StorageManager
import net.eduard.api.lib.database.api.SQLEngineType
import net.eduard.api.lib.database.api.SQLManager
import net.eduard.api.lib.modules.Extra
import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

abstract class HybridPlugin(
        val pluginBase: IPluginInstance
) : IPlugin {



    override fun getPlugin(): Any {
        return pluginBase
    }

    fun inBukkit(): Boolean {
        return pluginBase is JavaPlugin
    }

    fun inBungee(): Boolean {
        return pluginBase is Plugin
    }

    override fun getDataFolder(): File {

        return try {
            Extra.getMethodInvoke(pluginBase, "getDataFolder") as File
        }catch (ex :Exception){
            File("plugins/"+ getName()+"/")
        }

    }
    override fun getName(): String {
        return  Extra.getMethodInvoke(pluginBase, "getName") as String
    }

    override var isActivated = false
    override var isFree: Boolean = false
    final override var configs: Config = Config(getDataFolder(), "config.yml")
    final override var messages: Config = Config(getDataFolder(), "messages.yml")
    final override var storage: Config = Config(getDataFolder(), "storage.yml")
    final override var databaseFile = File(getDataFolder(), "database.db")
    final override var dbManager: DBManager = DBManager()
    final override var sqlManager: SQLManager = SQLManager(null, SQLEngineType.SQLITE)
    final override var storageManager: StorageManager = StorageManager()
    override fun log(message: String) {
        println("§a$message")
    }

    override fun error(message: String) {
        println("§c$message")
    }

    init {

        config.add("database-type", DBType.YAML)
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

    }

    override fun onLoad() {
        dbManager = config.get("database", DBManager::class.java)
        val dbType = config.get("database-type", DBType::class.java)
        storageManager.storeType = dbType
        if (db.isEnabled) {
            db.openConnection()
            var type = SQLEngineType.SQLITE
            try {
                 type = SQLEngineType.valueOf(dbType.name.toUpperCase())
            }catch (ex :Exception){}
            sqlManager = SQLManager(db.connection, type)
            storageManager.sqlManager = sqlManager
        }

        storageManager.folderBase = File(getDataFolder(),"/database/")
        storageManager.folderBase.mkdirs()
    }
    /**
     * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
     */
    override fun backup() {
        configs.set("backup-lasttime", Extra.getNow())
        try {
            val DATE_TIME_FORMATER = SimpleDateFormat("dd-MM-YYYY HH-mm-ss")
            val pasta = File(getDataFolder(),
                    "/backup/" + DATE_TIME_FORMATER.format(System.currentTimeMillis()) + "/")

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


    val config get() = configs




    /**
     * Deleta os ultimos backups
     */
    private fun deleteLastBackups() {
        val pasta = File(getDataFolder(), "/backup/")

        pasta.mkdirs()
        var lista = mutableListOf(*pasta.listFiles()!!)
        lista.sortBy { it.lastModified() }


        for (position in lista.size - 10 downTo 0) {
            val arquivo = lista[position]
            Extra.deleteFolder(arquivo)
            if (arquivo.exists())
                arquivo.delete()

        }
    }

    /**
     * Deleta os backups dos dias anteriores
     */
    override fun deleteOldBackups() {
        val pasta = File(getDataFolder(), "/backup/")
        pasta.mkdirs()
        var lista = listOf(*pasta.listFiles()!!)
        lista.filter { it.lastModified() + TimeUnit.DAYS.toMillis(1) <= System.currentTimeMillis() }
                .forEach {
                    Extra.deleteFolder(it)
                    if (it.exists())
                        it.delete()
                }
    }

}