package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiLoadPluginCommand : CommandManager("loadplugin", "carregarplugin") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        sender.sendMessage("§bEduardAPI §cNão implementado")
        return true
    }

    init {
        usage = "/api loadplugin <plugin>"
        description = "Carrega um plugin descarregado no servidor"
    }
}