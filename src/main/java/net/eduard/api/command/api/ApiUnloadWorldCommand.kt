package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiUnloadWorldCommand : CommandManager("unloadworld", "descarregarmundo", "desligarmundo") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        if (Mine.existsWorld(sender, args[0])) {
            Mine.unloadWorld(args[0], true)
            sender.sendMessage("§bEduardAPI §aVoce descarregou o mundo §2" + args[0])
        }
        return true
    }

    init {
        usage = "/api unloadworld <world>"
        description = "Descarrega um mundo carregado no servidor"
    }
}