package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiHelpCommand : CommandManager("help", "ajuda", "?") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        for (sub in getCommand("api").subCommands.values) {
            if (sender.hasPermission(sub.permission)) {
                sender.sendMessage("§a" + sub.usage + " §8-§7 " + sub.description)
            }
        }
        return true
    }

    init {
        description = "Mostra uma lista de comandos"
    }
}