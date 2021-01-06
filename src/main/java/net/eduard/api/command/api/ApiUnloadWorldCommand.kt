package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiUnloadWorldCommand : CommandManager("unloadworld", "descarregarmundo", "desligarmundo") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 1) {
            sendUsage(sender)
        } else {
            if (Mine.existsWorld(sender, args[1])) {
                Mine.unloadWorld(args[1])
                sender.sendMessage("§bEduardAPI §aVoce descarregou o mundo §2" + args[1])
            }
        }
        return true
    }

    init {
        usage = "/api unload <world>"
        description = "Descarrega um mundo carregado no servidor"
    }
}