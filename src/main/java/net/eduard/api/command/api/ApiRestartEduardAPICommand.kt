package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.util.ArrayList

class ApiRestartEduardAPICommand : CommandManager("restarteduardapi","restarteduardplugins") {
    init{
        description = "Reinicia todos os plugins dependentes do EduardAPI"
    }
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        sender.sendMessage("§eDescarregando plugins do Eduard")
        Mine.runCommand("timings off")
        val lista: MutableList<String> = ArrayList()
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin is EduardPlugin) {
                Mine.runCommand("plugman unload " + plugin.getName())
                lista.add(plugin.getName())
            }
        }
        sender.sendMessage("§ePlugins do Eduard descarregaados pelo PlugMan")
        sender.sendMessage("§fReiniciando EduardAPI")
        Mine.runCommand("plugman unload EduardAPI")
        Mine.runCommand("plugman load EduardAPI")
        sender.sendMessage("§fReiniciando plugins do Eduard")
        for (plNome in lista) {
            Mine.runCommand("plugman load $plNome")
        }
        sender.sendMessage("§aTodos os plugins foram recarregados com os novos jars")
        return true
    }
}