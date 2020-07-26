package net.eduard.api.lib.plugin

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

abstract class BukkitPlugin(val hybridPlugin: HybridPlugin) : JavaPlugin(), IPlugin by hybridPlugin {

    init{
        hybridPlugin.pluginBase = this
    }

    override fun console(message: String) {
        Bukkit.getConsoleSender().sendMessage(message)
    }


    override val pluginFolder: File
        get() = dataFolder

    override val pluginName: String
        get() = name



    override fun getPlugin(): JavaPlugin {
        return this
    }




 }
