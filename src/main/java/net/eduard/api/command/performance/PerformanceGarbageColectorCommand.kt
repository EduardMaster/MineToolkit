package net.eduard.api.command.performance

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.CommandSender

class PerformanceGarbageColectorCommand : CommandManager("garbagecolector", "gc") {

    init {
        description = "Indica para JVM forçar "
        usage = "/desempenho gc"
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        Runtime.getRuntime().gc()
        sender.sendMessage("§aAttivando GC de Memoria no Java.")

    }

}