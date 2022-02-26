package net.eduard.api.command.performance

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.CommandSender

class PerformanceMemoryCommand : CommandManager("memory", "ram", "men") {

    init {
        description = "Verifica o uso de Memoria RAM"
        usage = "/desempenho ram"
        register(PerformanceGarbageColectorCommand())
        register(PerformanceCPUCommand())
    }

    override fun command(sender: CommandSender, args: Array<String>) {

        val div = 1000 * 1000
        val memoriaDisponivel = (Runtime.getRuntime().freeMemory() / div)
        val totalMemoria = (Runtime.getRuntime().totalMemory() / div)
        val maximoMemoria = Runtime.getRuntime().maxMemory() / div
        val memoriaUsada = totalMemoria - memoriaDisponivel
        sender.sendMessage("§bVerificador de uso de Memoria")
        sender.sendMessage("§eMemoria Disponivel: §a${memoriaDisponivel}MB")
        sender.sendMessage("§eMemoria Total: §a${totalMemoria}MB")
        sender.sendMessage("§eMemoria Maxima: §c${maximoMemoria}MB")
        sender.sendMessage("§eMemoria Usada: §c${memoriaUsada}MB")

    }

}