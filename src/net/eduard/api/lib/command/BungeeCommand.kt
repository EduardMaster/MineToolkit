package net.eduard.api.lib.command

import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.Plugin

class BungeeCommand(val bungeeCommand: net.eduard.api.lib.command.Command)
    : Command(bungeeCommand.name, bungeeCommand.permission, *bungeeCommand.aliases.toTypedArray()) {


    fun register(plugin : Plugin){
        BungeeCord.getInstance().pluginManager.registerCommand(plugin,this)
    }

    override fun execute(sender: CommandSender, args: Array<String>) {

        if (sender is ProxiedPlayer) {
            bungeeCommand.processCommand(PlayerBungee(sender), args.toList())
        } else {
            bungeeCommand.processCommand(ConsoleSender(), args.toList())
        }
    }
}