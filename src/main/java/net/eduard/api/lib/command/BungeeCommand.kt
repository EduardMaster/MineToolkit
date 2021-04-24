package net.eduard.api.lib.command

import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.plugin.IPluginInstance
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin

class BungeeCommand(val command: net.eduard.api.lib.command.Command)
    : Command(command.name, null, *command.aliases.toTypedArray())  {

    fun register(plugin : IPluginInstance){
        register(plugin.plugin as Plugin)
    }
    fun register(plugin : Plugin){
        plugin.proxy.pluginManager.registerCommand(plugin,this)
    }

    override fun execute(sender: CommandSender, args: Array<String>) {
        if (!sender.hasPermission(command.permission)){
            sender.sendMessage(command.permissionMessage
                .replace("%permission", command.permission))
            return
        }
        if (sender is ProxiedPlayer) {
            command.processCommand(Hybrid.instance.getPlayer(sender.name, sender.uniqueId),
                args.toList())
        } else {
            command.processCommand(Hybrid.instance.console, args.toList())
        }
    }

}