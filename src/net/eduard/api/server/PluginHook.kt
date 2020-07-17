package net.eduard.api.server

import org.bukkit.Bukkit

abstract class PluginHook(val pluginName: String) {
    init {

        hooks.add(this)
        val pl = Bukkit.getPluginManager().getPlugin(pluginName)

        if (pl != null && pl.isEnabled) onPluginActive()

    }

    companion object {
        private var hooks = mutableListOf<PluginHook>()
        fun getRooks(plugin: String) =
                hooks.filter { it.pluginName.equals(plugin, ignoreCase = true) }

    }


    abstract fun onPluginActive()

}