package net.eduard.api.server

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.database.StorageManager
import net.eduard.api.lib.database.StorageType
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.storage.StorageAPI
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

/**
 * Representa os plugins feitos pelo Eduard
 *
 * @author Eduard
 * @version 1.0
 * @since 2.0
 */
open class EduardPlugin : JavaPlugin(), BukkitTimeHandler, IPlugin {

    private var started = false

    override val pluginName: String
        get() = Extra.getMethodInvoke(plugin, "getName" ) as String



    override fun getPlugin(): EduardPlugin {
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


    /**
     * Envia mensagem para o console caso as Log Normais esteja ativada para ele
     *
     * @param message Mensagem
     */
    override fun log(message: String) {
        if (isLogEnabled)
            Bukkit.getConsoleSender().sendMessage("§b[${name}] §f$message")
    }

    override fun getPluginConnected(): Plugin {
        return this
    }

    /**
     * Envia mensagem para o console caso as Log de Erros esteja ativada para ele
     *
     * @param message Mensagem
     */
    override fun error(message: String) {
        if (isLogEnabled)
            Bukkit.getConsoleSender().sendMessage("§b[${name}] §c$message")
    }


    /**
     * Necessario fazer super.onEnable() para poder suportar os comandos do PlugMan
     * <br></br> /plugman unload NomePlugin
     * <br></br>/plugman load NomePlugin
     */
    override fun onEnable() {
        if (!started) onLoad()

        log("Foi ativado na v" + description.version + " um plugin "
                + (if (isFree) "§aGratuito" else "§bPago") + "§f feito pelo Eduard")
    }


    fun getClasses(pack: String): List<Class<*>> {
        return Mine.getClasses(this, pack)
    }


    fun autosave() {
        configs.set("auto-save-lasttime", Extra.getNow())

        save()

    }


    override fun unregisterServices() {
        Bukkit.getServicesManager().unregisterAll(this)
    }

    override fun unregisterListeners() {
        HandlerList.unregisterAll(this)
    }

    override fun unregisterTasks() {
        Bukkit.getScheduler().cancelTasks(this)
    }

    override fun unregisterStorableClasses() {
        StorageAPI.debug("- CLASSES FROM PLUGIN $name")
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
        StorageAPI.debug("- CLASSES WITH SAME LOADER OF $name : $amount")
    }

    fun unregisterMenus() {
        for (menu in Menu.registeredMenus.toList()) {
            if (this == menu.plugin) {
                menu.unregisterMenu()
            }
        }


    }

    override fun unregisterCommands() {
        CommandManager.commandsRegistred.values.forEach { cmd ->
            if (this == cmd.plugin) {
                cmd.unregisterCommand()
            }
        }

    }


    override fun onDisable() {
        disconnectDB()
        unregisterServices()
        unregisterListeners()
        unregisterTasks()
        unregisterStorableClasses()
        unregisterMenus()
        unregisterCommands()
        log("Foi desativado na v" + description.version + " um plugin "
                + if (isFree) "§aGratuito" else "§bPago")

    }

    override fun onLoad() {
        started = true
        configs = Config(this, "config.yml")
        messages = Config(this, "messages.yml")
        storage = Config(this, "storage.yml")
        databaseFile = File(pluginFolder, "database.db")
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
        storageManager.type = configs.get("database-type", StorageType::class.java)
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






    /**
     * Deleta os ultimos backups
     */
    private fun deleteLastBackups() {
        val pasta = File(pluginFolder, "/backup/")

        pasta.mkdirs()
        val lista = mutableListOf(*pasta.listFiles()!!)
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
