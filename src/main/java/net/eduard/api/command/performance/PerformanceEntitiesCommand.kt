package net.eduard.api.command.performance

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class PerformanceEntitiesCommand : CommandManager("entities", "mobs") {

    init {
        description = "Verifica quantidade de Entidades Spawnados no servidor"
        usage = "/desempenho entities"
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        for (world in Bukkit.getWorlds()) {
            sender.sendMessage("§eMundo: ${world.name}")
            sender.sendMessage("§aEntidades (No-Living): §f" + world.entities.size)
            sender.sendMessage("§aEntidades: §f" + world.livingEntities.size)
        }
    }

}