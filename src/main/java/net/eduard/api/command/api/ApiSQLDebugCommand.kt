package net.eduard.api.command.api

import net.eduard.api.EduardAPI
import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.impl.MySQLEngine
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ApiSQLDebugCommand : CommandManager("sqldebug", "mysqldebug") {
    override fun command(sender: CommandSender, args: Array<String>) {
        if (DBManager.isDebugging) {
            DBManager.isDebugging = false
            EduardAPI.instance.configs["debug.database"] = false
            EduardAPI.instance.configs.saveConfig()
            sender.sendMessage("§cDebug de Database desativado")
        } else {
            DBManager.isDebugging = true
            EduardAPI.instance.configs["debug.database"] = true
            EduardAPI.instance.configs.saveConfig()
            sender.sendMessage("§aDebug de Database ativado")
        }
    }

    init {
        usage = "/api sqldebug"
        description = "Alterna o Debug de Database para Ativado/Desativado"
    }
}