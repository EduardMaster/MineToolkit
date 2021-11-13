package net.eduard.api.lib.command

import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.plugin.IPluginInstance
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class BukkitCommand(val command: net.eduard.api.lib.command.Command)
    : Command(command.name),
    CommandExecutor,
    TabCompleter, HybridCommand {


    override fun register(plugin: IPluginInstance) {
        register(plugin.plugin as JavaPlugin)
    }
    override fun unregister(plugin: IPluginInstance){
        unregister(plugin.plugin as JavaPlugin)
    }

    fun unregister(plugin: JavaPlugin){
        try {
            val commandMap = Extra.getFieldValue(Bukkit.getServer().pluginManager, "commandMap") as CommandMap
            unregister(commandMap)
            //commandMap.getCommand("cmd").unregister(commandMap)
            val currentCommands =  Extra.getFieldValue(commandMap, "knownCommands") as MutableMap<String, Command>
            val cmdName = name.toLowerCase()
            val pluginName = plugin.name.toLowerCase()
            for (aliase in aliases) {
                //log("Removendo aliase §a$aliase§f do comando §b$cmdName")
                currentCommands.remove(aliase.toLowerCase())
                currentCommands.remove(pluginName.toLowerCase() + ":" + aliase.toLowerCase())
            }
            try {
                currentCommands.remove(cmdName)
                currentCommands.remove(pluginName.toLowerCase() + ":" + cmdName)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }

    }

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

        if (!registred) {
            Bukkit.getScheduler().runTask(plugin) {
                val serverClass = Extra.getClassFrom(Bukkit.getServer())
                val field = serverClass.getDeclaredField("commandMap")
                field.isAccessible = true
                val map = field[Bukkit.getServer()] as CommandMap
                map.register(plugin.name, cmd)
            }
        }
    }


    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<String>): Boolean {

        if (sender is Player) {
            command.processCommand(
                Hybrid.instance.getPlayer(sender.name, sender.uniqueId),
                args.toList()
            )
        } else {
            command.processCommand(Hybrid.instance.console, args.toList())
        }

        return true
    }

    override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
        try {

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



    override fun onTabComplete(
        sender: CommandSender, cmd: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        val result = command.processTabComplete(
            if (sender is Player)
                Hybrid.instance.getPlayer(sender.name, sender.uniqueId)
            else Hybrid.instance.console, args.toList()
        )
        return result.ifEmpty { null }
    }
}