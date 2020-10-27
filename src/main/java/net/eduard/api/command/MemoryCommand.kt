package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MemoryCommand : CommandManager("memory", "memoria") {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val div = 1024
        val memoriaDisponivel = (Runtime.getRuntime().freeMemory() / div)
        val totalMemoria = (Runtime.getRuntime().totalMemory() / div)
        val maximoMemoria = Runtime.getRuntime().maxMemory() / div
        val memoriaUsada = totalMemoria - memoriaDisponivel
        sender.sendMessage("§aVerificador de uso de Memoria")
        sender.sendMessage("§bMemoria Disponivel: $memoriaDisponivel")
        sender.sendMessage("§bMemoria Total: $totalMemoria")
        sender.sendMessage("§aMemoria Maxima: $maximoMemoria")
        sender.sendMessage("§aMemoria Usada: $memoriaUsada")

        return true
    }
}