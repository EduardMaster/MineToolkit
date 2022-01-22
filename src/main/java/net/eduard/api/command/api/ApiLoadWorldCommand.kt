package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiLoadWorldCommand : CommandManager("loadworld", "carregarmundo", "ligarmundo") {

    init {
        usage = "/api loadworld <world>"
        description = "Carrega um mundo descarregado no servidor"
    }
    override fun command(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(sender)
            return
        }
        val worldName = args[0]
        if (Bukkit.getWorld(worldName) == null) {
            Mine.loadWorld(worldName)
            sender.sendMessage("§bEduardAPI §aVoce carregou o mundo §f$worldName")
        } else {
            sender.sendMessage("§bEduardAPI §aO mundo §f$worldName §cjá esta carregado")
        }
    }


}