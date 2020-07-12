package net.eduard.api.lib.command

import net.eduard.api.lib.modules.Mine
import org.bukkit.command.*
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BukkitCommand(val command: net.eduard.api.lib.command.Command)
    : Command(command.name), CommandExecutor, TabCompleter {
    fun register(plugin: JavaPlugin) {
        var cmd: Command? = plugin.getCommand(command.name)
        var registred = false
        if ((cmd != null)) {
            registred = true
            (cmd as PluginCommand).executor = this
            cmd.tabCompleter = this
        } else cmd = this
        cmd.permission = command.permission
        cmd.permissionMessage = command.permissionMessage
        cmd.aliases = command.aliases
        cmd.usage = command.usage
        cmd.description = command.description

        if (!registred)
            Mine.createCommand(plugin, this)


    }

    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        if (sender is Player) {
            command.processCommand(PlayerBukkit(sender), args.toList())

        } else {
            command.processCommand(ConsoleSender, args.toList())
        }

        return true
    }

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        try {
            /*
            if (!plugin.isEnabled) {
                return false
            }

             */
            if (sender.hasPermission(permission)) {

                return onCommand(sender, this, label, args)

            } else {
                sender.sendMessage(permissionMessage)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return false
    }

    @Throws(IllegalArgumentException::class)
    override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String>? {

        return onTabComplete(sender, this, alias, args)
    }

    override fun onTabComplete(sender: CommandSender, cmd: Command,
                               label: String,
                               args: Array<String>): List<String>? {


        val result = command.processTabComplete(
                if (sender is Player)
                    PlayerBukkit(sender)
                else ConsoleSender, args.toList())
        return if (result.isEmpty()) null
        else result

    }
}