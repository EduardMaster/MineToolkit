package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiUnloadPluginCommand : CommandManager("unloadplugin", "descarregarplugin") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage("§bEduardAPI §cNão implementado")
        return true
    }

    init {
        usage = "/api unloadplugin <plugin>"
        description = "Descarrega um plugin carregado no servidor"
    }
}