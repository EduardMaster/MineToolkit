package net.eduard.api.lib.manager

import net.eduard.api.EduardAPI
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin



/**
 * Controlador de Eventos ([Listener])
 *
 * @author Eduard
 * @version 2.0
 */
open class EventsManager(  @Transient
                           var plugin: Plugin? = null
 ) : Listener {
    /**
     * Se o Listener esta registrado
     */
    /**
     * @return Se a Listener esta registrado
     */
    @Transient
    var isRegistered: Boolean = false
        private set
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

    val pluginInstance : Plugin

    get(){
        if (plugin == null) {
            plugin = defaultPlugin()
        }
        return plugin!!
    }


    /**
     * Construtor base deixando Plugin automatico
     */
    init {
        if (plugin == null){
            plugin = defaultPlugin()
        }

    }

    protected fun defaultPlugin(): Plugin {
        var defPl: JavaPlugin? = JavaPlugin.getProvidingPlugin(javaClass)
        if (defPl == null) {
            defPl = EduardAPI.instance
        }
        return defPl
    }


    /**
     * Registra o Listener para o Plugin
     *
     * @param plugin Plugin
     */
    open fun register(plugin: Plugin) {
        unregisterListener()
        this.isRegistered = true
        this.plugin = plugin
        Bukkit.getPluginManager().registerEvents(this, plugin)
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

