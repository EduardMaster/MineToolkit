package net.eduard.api.server

import org.bukkit.Bukkit

abstract class PluginHook(val pluginName: String) {
    init {
       prepare()
    }
    fun prepare(){
        hooks.add(this)
        val plugin = Bukkit.getPluginManager().getPlugin(pluginName)
        if (plugin != null && plugin.isEnabled) {
            onPluginActive()
        }
    }

    companion object {
        private var hooks = mutableListOf<PluginHook>()
        fun getRooks(plugin: String) =
                hooks.filter { it.pluginName.equals(plugin, ignoreCase = true) }

    }


    abstract fun onPluginActive()

}