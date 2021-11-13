package net.eduard.api.server

import net.eduard.api.lib.command.Command
import net.eduard.api.lib.config.Config
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.kotlin.resolvePut
import net.eduard.api.lib.kotlin.resolveTake
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.plugin.PluginSettings
import net.eduard.api.lib.storage.StorageAPI
import net.minecraft.server.v1_8_R3.CommandDebug
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * Representa os plugins feitos pelo Eduard
 *
 * @author Eduard
 * @version 1.0
 * @since 2.0
 */
open class EduardPlugin : JavaPlugin(), BukkitTimeHandler, IPlugin {
    fun getString(key: String) = configs.getString(key)
    fun message(key: String) = messages.message(key)
    fun getInt(key: String) = configs.getInt(key)
    fun getLong(key: String) = configs.getLong(key)
    fun getBoolean(key: String) = configs.getBoolean(key)
    fun getMessages(key: String) = messages.getMessages(key)
    var calulatingLag = false
    var isFree = false
    var activated = false
    var reloaded = false
    var started = false

    fun backup() {

    }

    override fun getPlugin(): EduardPlugin {
        return this
    }

    open fun onActivation() {

    }

    override fun getSystemName(): String {
        return description.name
    }


    lateinit var configs: Config
    lateinit var messages: Config
    lateinit var storage: Config
    lateinit var settings: PluginSettings
    lateinit var dbManager: DBManager
    lateinit var sqlManager: SQLManager
    override fun onLoad() {
        //
        val currentInstance: EduardPlugin = this
        if (!currentInstance.started) {
            currentInstance.dbManager = DBManager()
            currentInstance.configs = Config(currentInstance, "config.yml")
            currentInstance.messages = Config(currentInstance, "messages.yml")
            currentInstance.storage = Config(currentInstance, "storage.yml")
            currentInstance.settings = PluginSettings()
            currentInstance.configs.add("settings", currentInstance.settings)
            currentInstance.configs.add("database", currentInstance.dbManager)
            currentInstance.configs.saveConfig()
            currentInstance.settings = currentInstance.configs.get("settings", PluginSettings::class.java)
            currentInstance.dbManager = currentInstance.configs.get("database", DBManager::class.java)
            currentInstance.sqlManager = SQLManager(currentInstance.dbManager)
            //  currentInstance.setStorageManager(new StorageManager(currentInstance.getSqlManager()));
            currentInstance.started = true
            // currentInstance.getStorageManager().setType(currentInstance.getSettings().getStoreType());
            if (currentInstance.dbManager.isEnabled) {
                currentInstance.dbManager.openConnection()
            }
        }
    }

    /**
     * Envia mensagem para o console caso as Log Normais esteja ativada para ele
     *
     * @param message Mensagem
     */
    open fun log(message: String) {
        if (settings.isDebug)
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
    open fun error(message: String) {
        if (settings.isDebug)
            Bukkit.getConsoleSender().sendMessage("§b[${name}] §c$message")
    }


    /**
     * Necessario fazer super.onEnable() para poder suportar os comandos do PlugMan
     * <br></br> /plugman unload NomePlugin
     * <br></br>/plugman load NomePlugin
     */
    override fun onEnable() {
        if (!started) onLoad()
        log(
            "Foi ativado na v" + description.version + " um plugin "
                    + (if (isFree) "§aGratuito" else "§bPago") + "§f feito pelo Eduard"
        )
        resolvePut(this)
    }

    fun getClasses(pack: String): MutableList<Class<*>> {
        return Mine.getClasses(this, pack)
    }


    fun autosave() {
        settings.lastSave = Extra.getNow()

        save()

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


    fun unregisterMenus() {
        for (menu in Menu.registeredMenus.toList()) {
            if (this == menu.plugin) {
                menu.unregisterMenu()
            }
        }
    }

    /**
     * Corrigido descarregamento de comando ao /plugman reload NomePlugin
     * Dia 13/11/2021
     *
     */
    fun unregisterCommands() {
        CommandManager.unregisterCommands(this)
        Command.unregisterCommands(this)
    }


    inline fun calculate(actionName: String, action: () -> Unit) {
        val inicio = System.currentTimeMillis()
        action.invoke()
        val fim = System.currentTimeMillis()
        val lag = fim - inicio
        if (calulatingLag) {
            log("Lag causado em $actionName -> ${lag}ms")
        }
    }

    override fun onDisable() {
        calculate("Desregistrando Servicos") { unregisterServices() }
        calculate("Desregistrando Listeners") { unregisterListeners() }
        calculate("Desregistrando Comandos") { unregisterCommands() }
        calculate("Desregistrando Tasks") { unregisterTasks() }
        calculate("Desregistrando Storables") { unregisterStorableClasses() }
        calculate("Desregistrando menus") { unregisterMenus() }

        calculate("Desconectando Database Connections e Limpando Cache") { dbManager.closeConnection() }
        log(
            "Foi desativado na v" + description.version + " um plugin "
                    + if (isFree) "§aGratuito" else "§bPago"
        )
        resolveTake(this)
    }


    open fun save() {

    }

    override fun getPluginFolder() = dataFolder

    open fun reload() {
        reloaded = true
    }

    open fun configDefault() {

    }


    /**
     * Deleta os ultimos backups
     */
    private fun deleteLastBackups() {
        val pasta = File(getPluginFolder(), "/backup/")
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

    fun unregisterStorableClasses() {
        StorageAPI.unregisterPlugin(javaClass)
    }

    /**
     * Deleta os backups dos dias anteriores
     */
    fun deleteOldBackups() {
        val pasta = File(getPluginFolder(), "/backup/")
        pasta.mkdirs()
        val lista = listOf(*pasta.listFiles()!!)
        lista.filter {
            it.lastModified() + TimeUnit.DAYS
                .toMillis(1) <= System.currentTimeMillis()
        }
            .forEach {
                Extra.deleteFolder(it)
                if (it.exists())
                    it.delete()
            }
    }

}
