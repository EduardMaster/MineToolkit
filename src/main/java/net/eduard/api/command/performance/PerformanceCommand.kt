package net.eduard.api.command.performance

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.CommandSender

class PerformanceCommand : CommandManager("performance", "desempenho","checklag") {

    init {
        description = "Verifica Desempenho do Servidor e derivados"
        usage = "/desempenho "
        register(PerformanceGarbageColectorCommand())
        register(PerformanceCPUCommand())
        register(PerformanceMemoryCommand())
        register(PerformanceEntitiesCommand())
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        for (subCmd in subCommands.values){
            sender.sendMessage("§b${subCmd.usage} §8- §7${subCmd.description}")
        }
    }
}