package net.eduard.api.lib.command

import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin

class BungeeCommand(val command: net.eduard.api.lib.command.Command)
    : Command(command.name, command.permission, *command.aliases.toTypedArray()) {


    fun register(plugin : Plugin){
        plugin.proxy.pluginManager.registerCommand(plugin,this)
    }

    override fun execute(sender: CommandSender, args: Array<String>) {

        if (sender is ProxiedPlayer) {
            command.processCommand(PlayerBungee(sender), args.toList())
        } else {
            command.processCommand(ConsoleSender, args.toList())
        }
    }
}