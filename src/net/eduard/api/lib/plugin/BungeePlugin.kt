package net.eduard.api.lib.plugin

import net.md_5.bungee.api.plugin.Plugin
import java.io.File

abstract class BungeePlugin(val hybridPlugin: HybridPlugin) : Plugin(), IPlugin by hybridPlugin {


    init{
        hybridPlugin.pluginBase = this
    }

    override val pluginFolder: File
        get() = dataFolder

    override val pluginName: String
        get() = description.name

    override fun getPlugin(): BungeePlugin {
        return this
    }


}