package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MemoryCommand : CommandManager("memory", "memoria") {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isNotEmpty()){
            if (args[0] == "gc"){
                Runtime.getRuntime().gc()
                sender.sendMessage("§aGC Run")
            }
        }
        val div = 1000*1000
        val memoriaDisponivel = (Runtime.getRuntime().freeMemory() / div)
        val totalMemoria = (Runtime.getRuntime().totalMemory() / div)
        val maximoMemoria = Runtime.getRuntime().maxMemory() / div
        val memoriaUsada = totalMemoria - memoriaDisponivel
        sender.sendMessage("§bVerificador de uso de Memoria")
        sender.sendMessage("§eMemoria Disponivel: §a${memoriaDisponivel}MB")
        sender.sendMessage("§eMemoria Total: §a${totalMemoria}MB")
        sender.sendMessage("§eMemoria Maxima: §c${maximoMemoria}MB")
        sender.sendMessage("§eMemoria Usada: §c${memoriaUsada}MB")



        return true
    }
}