package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiLoadWorldCommand : CommandManager("loadworld", "carregarmundo", "ligarmundo") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
        } else {
            if (Bukkit.getWorld(args[0]) == null) {
                Mine.loadWorld(args[0])
                sender.sendMessage("§bEduardAPI §aVoce carregou o mundo §2" + args[0])
            } else {
                sender.sendMessage("§bEduardAPI §aEste mundo já esta carregado")
            }
        }
        return true
    }

    init {
        usage = "/api loadworld <world>"
        description = "Carrega um mundo descarregado no servidor"
    }
}