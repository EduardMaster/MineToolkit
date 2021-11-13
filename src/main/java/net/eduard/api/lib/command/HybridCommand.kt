package net.eduard.api.lib.command

import net.eduard.api.lib.plugin.IPluginInstance

interface HybridCommand {

    fun register(plugin : IPluginInstance)
    fun unregister(plugin : IPluginInstance)

}