package net.eduard.api.command

import com.sun.management.OperatingSystemMXBean
import net.eduard.api.lib.kotlin.formatDuration
import net.eduard.api.lib.kotlin.percent
import net.eduard.api.lib.manager.CommandManager
import org.bukkit.command.CommandSender
import java.lang.management.ManagementFactory

class PerformanceCommand : CommandManager("performance", "desempenho") {

    init {
        description = "Verifica Desempenho do Processo Java e Servidor"
        usage = "/desempenho [memory|cpu|gc]"
    }

    override fun command(sender: CommandSender, args: Array<String>) {
        if (args.isEmpty()){
            sendUsage(sender)
            return
        }
            if (args[0] == "gc") {
                Runtime.getRuntime().gc()
                sender.sendMessage("§aAttivando GC de Memoria no Java.")
                return;
            }
            if (args[0] == "cpu") {
               // val operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                try {
                    val operatingSystemMXBean: OperatingSystemMXBean = ManagementFactory.getPlatformMXBean(
                        OperatingSystemMXBean::class.java
                    )

                    sender.sendMessage("§eCPU: §a" + operatingSystemMXBean.name)
                    sender.sendMessage("§eArquitetura: §a" + operatingSystemMXBean.arch)
                    sender.sendMessage("§eProcessadores: §a" + operatingSystemMXBean.availableProcessors)
                    sender.sendMessage("§bMedia Em Uso no Computador: §a" + operatingSystemMXBean.systemLoadAverage.percent() + "%")
                    sender.sendMessage("§bEm Uso no Computador: §a" + operatingSystemMXBean.systemCpuLoad.percent() + "%")
                    sender.sendMessage("§bEm Uso no Processo: §a" + operatingSystemMXBean.processCpuLoad.percent() + "%")
                    sender.sendMessage("§dServidor Tempo Ligado: §a" + (operatingSystemMXBean.processCpuTime/ 1_000_000   ).formatDuration())
                }catch (ex : Exception){
                    sender.sendMessage("§cFalha ao puxar dados da Classe Sun sobre o Sistema Operacional.")
                    ex.printStackTrace()
                }

                return;
            }
            if (args[0].equals("memory")|| args[0] == "ram"){
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

}