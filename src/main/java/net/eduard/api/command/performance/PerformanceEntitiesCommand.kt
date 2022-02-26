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
            val entidades = world.entities
            sender.sendMessage("§aEntidades (No-Living): §f" + entidades.size)
            for ((type, list) in entidades.groupBy { it.type }){
                sender.sendMessage("§bEntidade do Tipo §3${type.name}: §f${list.size}" )
            }
            val entidadesVivas = world.livingEntities
            sender.sendMessage("§aEntidades: §f" + entidadesVivas.size)
            for ((type, list) in entidadesVivas.groupBy { it.type }){
                sender.sendMessage("§bEntidade do Tipo §3${type.name}: §f${list.size}" )
            }
        }
    }

}