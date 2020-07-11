package net.eduard.api.lib.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BukkitCommand(val command: net.eduard.api.lib.command.Command) : CommandExecutor {
    fun register(plugin: JavaPlugin) {
        val cmd = plugin.getCommand(command.name)
        if (cmd != null) {
            cmd.permission = command.permission
            cmd.permissionMessage = command.permissionMessage
            cmd.setAliases(command.aliases)
            cmd.executor = this
        } else {

        }
    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        if (sender is Player) {
            command.processCommand(PlayerBukkit(sender), args.toList())

        } else {
            command.processCommand(ConsoleSender(), args.toList())
        }

        return true
    }

}