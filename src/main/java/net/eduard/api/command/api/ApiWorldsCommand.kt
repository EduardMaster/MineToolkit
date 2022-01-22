package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.lang.StringBuilder

class ApiWorldsCommand : CommandManager("worlds", "mundos","worldlist") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val mundos = StringBuilder()
        for (world in Bukkit.getWorlds()) {
            if (mundos.isNotEmpty()) {
                mundos.append("§a, §f")
            }
            mundos.append("" + world.name)
        }
        sender.sendMessage("§bEduardAPI §aOs mundo existentes são: §f$mundos")
        return true
    }

    init {
        description = "Mostra uma lista de mundos carregados no servidor"
    }
}