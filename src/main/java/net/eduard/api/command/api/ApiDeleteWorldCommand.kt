package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiDeleteWorldCommand : CommandManager("deleteworld", "deletarmundo") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        val worldName = args[0]
        if (Mine.existsWorld(sender, worldName)) {
            Mine.deleteWorld(worldName)
            sender.sendMessage("§bEduardAPI §aO Mundo §2$worldName§a foi deletado com sucesso!")
        }

        return true
    }

    init {
        usage = "/api deleteworld <world>"
        description = "Deleta um mundo do servidor"
    }
}