package net.eduard.api.server

import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.Menu
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.api.SQLEngineType
import net.eduard.api.lib.database.api.SQLManager
import net.eduard.api.lib.database.mysql.MySQLOption
import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.storage.StorageAPI

/**
 * Representa os plugins feitos pelo Eduard
 *
 * @author Eduard
 * @version 1.0
 * @since 2.0
 */
abstract class EduardPlugin : JavaPlugin(), BukkitTimeHandler {
    var isActivated = false
    var isLogEnabled = true
    var isErrorLogEnabled = true
    var isSQLManagerStarted = false
    var db = DBManager()
        @JvmName("getDB")
        get

     lateinit var sqlManager: SQLManager



    protected fun startSQLManager() {
        isSQLManagerStarted = true
        var type = if (db.useSQLite()) SQLEngineType.SQLITE else SQLEngineType.MYSQL
        sqlManager = SQLManager(db.connection, type)

    }


    lateinit var configs: Config
        protected set
     lateinit var messages: Config
        protected set

    /**
     * @return A [Config] Storage
     */
    lateinit var storage: Config
        protected set
    var databaseFile = File(dataFolder, "database.db")
    /**
     * @return Se o Plugin é gratuito
     */
    /**
     * Define se o Plugin é gratuito
     *
     * @param free
     */
    var isFree: Boolean = false

    /**
     * Verifica se tem algo dentro da config.yml
     *
     * @return Se a a config.yml tem configurações
     */
    val isEditable: Boolean
        get() = !configs.keys.isEmpty()

    val autoSaveSeconds: Long
        get() = configs.getLong("auto-save-seconds")!!

    val isAutoSaving: Boolean
        get() = configs.getBoolean("auto-save")

    val backupTime: Long
        get() = configs.getLong("backup-time")!!

    val backupLastTime: Long
        get() = configs.getLong("backup-lasttime")!!

    val autoSaveLastTime: Long
        get() = configs.getLong("auto-save-lasttime")!!

    val backupTimeUnitType: TimeUnit
        get() = TimeUnit.valueOf(configs.getString("backup-timeunit-type").toUpperCase())

    val canBackup: Boolean
        get() = configs.getBoolean("backup")

    fun getString(path: String): String {

        return configs.message(path)
    }


    /**
     * Config padrão do spigot não funciona com Config
     */
    override fun getConfig(): FileConfiguration? {
        return null
    }

    /**
     * Envia mensagem para o console caso as Log Normais esteja ativada para ele
     *
     * @param msg Mensagem
     */
    fun log(msg: String) {
        if (isLogEnabled)
            Bukkit.getConsoleSender().sendMessage("§b[$name] §f$msg")
    }

    /**
     * Envia mensagem para o console caso as Log de Erros esteja ativada para ele
     *
     * @param msg Mensagem
     */
    fun error(msg: String) {
        if (isErrorLogEnabled)
            Bukkit.getConsoleSender().sendMessage("§b[$name] §c$msg")
    }


    override fun getPluginConnected(): Plugin {
        return this
    }


    protected fun reloadVars() {
        configs = Config(this, "config.yml")
        messages = Config(this, "messages.yml")
        storage = Config(this, "storage.yml")
    }

    /**
     * Necessario fazer super.onEnable() para poder suportar os comandos do PlugMan
     * <br></br> /plugman unload NomePlugin
     * <br></br>/plugman load NomePlugin
     */
    override fun onEnable() {
        reloadVars()
        mainConfig()

        reloadDBManager()
        log("Foi ativado na v" + description.version + " um plugin "
                + (if (isFree) "§aGratuito" else "§bPago") + "§f feito pelo Eduard")
    }

    protected fun mainConfig() {


        configs.add("auto-save", false)
        configs.add("auto-save-seconds", 60)
        configs.add("auto-save-lasttime", Extra.getNow())
        configs.add("backup", false)
        configs.add("backup-lasttime", Extra.getNow())
        configs.add("backup-time", 1)
        configs.add("backup-timeunit-type", "MINUTES")
        configs.add("database", db)
        configs.saveConfig()
    }


    fun reloadDBManager() {
        db = configs.get("database", DBManager::class.java)
    }

    fun registerPackage(packname: String) {
        StorageAPI.registerPackage(javaClass, packname)
    }

    fun getClasses(pack: String): List<Class<*>> {
        return Mine.getClasses(this, pack)
    }


    fun autosave() {
        configs.set("auto-save-lasttime", Extra.getNow())

        save()

    }

    /**
     * Deleta os ultimos backups
     */
    private fun deleteLastBackups() {
        val pasta = File(dataFolder, "/backup/")

        pasta.mkdirs()
        var lista = mutableListOf(*pasta.listFiles()!!)
        lista.sortBy { it.lastModified() }

        //lista = lista.stream().sorted(Comparator.comparing<File, Long>(Function<File, Long> { return@Function it.lastModified() })).collect<List<File>, Any>(Collectors.toList())
        for (i in lista.size - 10 downTo 0) {
            val arquivo = lista[i]
            Extra.deleteFolder(arquivo)
            if (arquivo.exists())
                arquivo.delete()

        }
    }

    /**
     * Deleta os backups dos dias anteriores
     */
    fun deleteOldBackups() {
        val pasta = File(dataFolder, "/backup/")
        pasta.mkdirs()
        var lista = listOf(*pasta.listFiles()!!)
        lista.filter { it.lastModified() + TimeUnit.DAYS.toMillis(1) <= System.currentTimeMillis() }
                .forEach {
                    Extra.deleteFolder(it)
                    if (it.exists())
                        it.delete()
                }
    }

    /**
     * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
     */
    fun backup() {
        configs.set("backup-lasttime", Extra.getNow())
        try {

            val pasta = File(dataFolder,
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

    fun unregisterServices() {
        Bukkit.getServicesManager().unregisterAll(this)
    }

    fun unregisterListeners() {
        HandlerList.unregisterAll(this)
    }

    fun unregisterTasks() {
        Bukkit.getScheduler().cancelTasks(this)
    }

    fun unregisterStorableClasses() {
        StorageAPI.unregisterStorables(this)
    }

    fun unregisterMenus() {
        for (menu in Menu.registeredMenus.toList()) {
            if (this == menu.pluginInstance) {
                menu.unregisterListener()
            }
        }


    }

    fun unregisterCustomCommands() {
        CommandManager.commandsRegistred.values.forEach { cmd ->
            if (this == cmd.pluginInstance) {
                cmd.unregisterCommand()
            }
        }

    }

    fun disconnectDB() {
        db.closeConnection()
    }

    override fun onDisable() {
        disconnectDB()
        unregisterServices()
        unregisterListeners()
        unregisterTasks()
        unregisterStorableClasses()
        unregisterMenus()
        unregisterCustomCommands()
        log("Foi desativado na v" + description.version + " um plugin "
                + if (isFree) "§aGratuito" else "§bPago")


    }


    open fun save() {

    }

    open fun reload() {}

    open fun onActivation(){}

    open fun configDefault() {

    }

    fun getBoolean(path: String): Boolean {
        return configs.getBoolean(path)
    }

    fun getInt(path: String): Int {
        return configs.getInt(path)!!
    }

    fun getDouble(path: String): Double {
        return configs.getDouble(path)!!
    }

    fun message(path: String): String {
        return messages.message(path)
    }

    fun getMessages(path: String): List<String> {
        return messages.getMessages(path)
    }

    companion object {
        private val DATE_TIME_FORMATER = SimpleDateFormat("dd-MM-YYYY HH-mm-ss")
    }


}
