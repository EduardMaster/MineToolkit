package net.eduard.api.server

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.modules.BukkitTimeHandler
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.plugin.BukkitPlugin
import net.eduard.api.lib.plugin.HybridPlugin
import net.eduard.api.lib.storage.StorageAPI
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.Plugin
import java.io.File

/**
 * Representa os plugins feitos pelo Eduard
 *
 * @author Eduard
 * @version 1.0
 * @since 2.0
 */
open class EduardPlugin : BukkitPlugin( EduardHybridPlugin()), BukkitTimeHandler {



    override fun getPlugin(): EduardPlugin {
        return this
    }

    init{
        hybridPlugin.pluginBase = this

    }

     class EduardHybridPlugin : HybridPlugin() {


        override fun onEnable() {

        }

        override fun onDisable() {

        }

    }






    /**
     * Envia mensagem para o console caso as Log Normais esteja ativada para ele
     *
     * @param message Mensagem
     */
    override fun log(message: String) {
        if (isLogEnabled)
            Bukkit.getConsoleSender().sendMessage("§b[${getName()}] §f$message")
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
            Bukkit.getConsoleSender().sendMessage("§b[${getName()}] §c$message")
    }


    /**
     * Necessario fazer super.onEnable() para poder suportar os comandos do PlugMan
     * <br></br> /plugman unload NomePlugin
     * <br></br>/plugman load NomePlugin
     */
    override fun onEnable() {
       super.onEnable();
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


}
