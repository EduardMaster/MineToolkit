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
    @Transient
    var isRegistered: Boolean = false

    @Transient
    var plugin: JavaPlugin = defaultPlugin()


    private fun defaultPlugin(): JavaPlugin {

        return JavaPlugin.getProvidingPlugin(javaClass)
    }

    open fun register(plugin: IPluginInstance) {
        registerListener(plugin.plugin as JavaPlugin)
    }

    /**
     * Registra o Listener para o Plugin
     *
     * @param plugin Plugin
     */
    fun registerListener(plugin: JavaPlugin) {
        unregisterListener()
        this.plugin = plugin
        registerListener()
    }

    private fun registerListener() {
        this.isRegistered = true
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }


    /**
     * Desregistra o Listener
     */
    fun unregisterListener() {
        if (isRegistered) {
            HandlerList.unregisterAll(this)
            this.isRegistered = false
        }
    }

}

