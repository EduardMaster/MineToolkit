package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.EduardPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiSaveCommand : CommandManager("save", "salvar") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 1) {
            sendUsage(sender)
        } else {
            val sub = args[1]
            if (Mine.existsPlugin(sender, sub)) {
                val plugin = Mine.getPlugin(sub)
                if (plugin is EduardPlugin) {
                    plugin.save()
                    sender.sendMessage("§bEduardAPI §aPlugin do Eduard " + plugin.getName() + "§a foi salvado")
                } else {
                    sender.sendMessage("§bEduardAPI §cEste plugin nao é um plugin do Eduard")
                }
            }
        }
        return true
    }

    init {
        usage = "/api save <plugin>"
        description = "Salva um plugin feito pelo Eduard"
    }
}