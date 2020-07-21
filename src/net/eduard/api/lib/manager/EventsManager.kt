package net.eduard.api.lib.manager

import net.eduard.api.lib.plugin.IPluginInstance
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
/**
 * Controlador de Eventos ([Listener])
 *
 * @author Eduard
 * @version 2.0
 */
open class EventsManager : Listener {
    /**
     * Se o Listener esta registrado
     */
    /**
     * @return Se a Listener esta registrado
     */
    @Transient
    var isRegistered: Boolean = false
    /**
     * Plugin
     */
    /**
     * Seta o Plugin
     *
     * @param plugin Plugin
     */



    /**
     * @return Plugin
     */

    val pluginInstance get() = plugin



    /**
     * Construtor base deixando Plugin automatico
     */

    @Transient
    var plugin: JavaPlugin = defaultPlugin()


    protected fun defaultPlugin(): JavaPlugin {

        return JavaPlugin.getProvidingPlugin(javaClass)
    }



    /**
     * Registra o Listener para o Plugin
     *
     * @param plugin Plugin
     */
    open fun registerListener(plugin: JavaPlugin) {
        unregisterListener()
        registerListener()
    }
    open fun registerListener() {
        this.plugin = plugin
        this.isRegistered = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }
    open fun register(plugin : IPluginInstance){
        registerListener(plugin.plugin as JavaPlugin)
    }


    /**
     * Desregistra o Listener
     */
    open fun unregisterListener() {
        if (isRegistered) {
            HandlerList.unregisterAll(this)
            this.isRegistered = false
        }
    }

}

