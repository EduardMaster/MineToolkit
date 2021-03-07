package net.eduard.api.server

import net.eduard.api.lib.config.Config
import net.eduard.api.lib.config.StorageManager
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.plugin.IPlugin
import net.eduard.api.lib.plugin.PluginSettings
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

    var isFree= false
    override var started = false

    override val pluginName: String
        get() = Extra.getMethodInvoke(plugin, "getName") as String
    override val pluginFolder: File
        get() = plugin.dataFolder


    override fun getPlugin(): EduardPlugin {
        return this
    }

    override fun onLoad() {
        super<IPlugin>.onLoad()
    }

    final override lateinit var configs: Config
    final override lateinit var messages: Config
    final override lateinit var storage: Config
    final override lateinit var settings: PluginSettings
    final override var dbManager: DBManager = DBManager()
    final override lateinit var sqlManager: SQLManager
    final override lateinit var storageManager: StorageManager


    /**
     * Envia mensagem para o console caso as Log Normais esteja ativada para ele
     *
     * @param message Mensagem
     */
    override fun log(message: String) {
        if (settings.debug)
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
        if (settings.debug)
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
    }


    fun getClasses(pack: String): List<Class<*>> {
        return Mine.getClasses(this, pack)
    }


    fun autosave() {
        settings.lastSave =  Extra.getNow()

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
        log(
            "Foi desativado na v" + description.version + " um plugin "
                    + if (isFree) "§aGratuito" else "§bPago"
        )

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
        lista.filter { it.lastModified() + TimeUnit.DAYS
            .toMillis(1) <= System.currentTimeMillis() }
            .forEach {
                Extra.deleteFolder(it)
                if (it.exists())
                    it.delete()
            }
    }

}
