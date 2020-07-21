package net.eduard.api.server

import net.eduard.api.lib.plugin.BungeePlugin
import net.eduard.api.lib.plugin.HybridPlugin
import net.eduard.api.lib.plugin.IPluginInstance
import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Listener

/**
 * Representa os Plugins meus feitos para o Bungeecord
 *
 * @author Eduard
 */
open class EduardBungeePlugin : BungeePlugin() , IPluginInstance {

    override val hybridPlugin: EduardHybridPlugin = EduardHybridPlugin(this)





    override fun getPlugin(): EduardBungeePlugin {
        return this
    }
    class EduardHybridPlugin(plugin : EduardBungeePlugin) : HybridPlugin(plugin) {



        override fun onLoad() {

        }

        override fun onEnable() {

        }

        override fun onDisable() {

        }


    }

    fun registerEvents(events: Listener) {
        BungeeCord.getInstance().getPluginManager().registerListener(this, events)
    }

    fun registerCommand(comand: Command) {
        BungeeCord.getInstance().getPluginManager().registerCommand(this, comand)
    }


    fun console(msg: String) {
        BungeeCord.getInstance().console.sendMessage(TextComponent(msg))
    }

}
