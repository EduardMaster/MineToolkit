package net.eduard.api.command.api

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.EduardPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiReloadCommand : CommandManager("reload", "recarregar") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        val pluginName = args[0]
        if (Mine.existsPlugin(sender, pluginName)) {
            val plugin = Mine.getPlugin(pluginName)
            if (plugin is EduardPlugin) {
                plugin.reload()
                sender.sendMessage("§bEduardAPI §aPlugin do Eduard §f" + plugin.getName() + " §afoi recarregado")
            } else {
                sender.sendMessage("§cEste plugin nao é um plugin do Eduard")
            }
        }

        return true
    }

    init {
        usage = "/api reload <plugin>"
        description = "Recarrega um plugin feito pelo Eduard"
    }
}