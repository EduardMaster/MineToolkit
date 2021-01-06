package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiLoadCommand : CommandManager("load", "carregar") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        sender.sendMessage("§bEduardAPI §cNão implementado")
        return true
    }

    init {
        usage = "/api load <plugin>"
        description = "Carrega um plugin descarregado no servidor"
    }
}