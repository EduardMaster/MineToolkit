package net.eduard.api.lib.manager

import net.eduard.api.lib.kotlin.formatColors
import java.util.ArrayList
import java.util.HashMap

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.PluginCommand
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.Plugin

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

open class CommandManager(var name: String, vararg aliases: String) : EventsManager(), TabCompleter, CommandExecutor {

    @Transient
    var parent: CommandManager? = null
        set(value) {
            field = value
            if (usage.isEmpty()) {
                usage = autoUsage()
            }
            if (permission.isEmpty()) {
                permission = autoPermission()
            }
        }

    @Transient
    var customCommand: CustomCommand? = null
    var subCommands: MutableMap<String, CommandManager> = HashMap()

    open fun playerCommand(player: Player, args: Array<String>) {

    }

    open fun command(sender: CommandSender, args: Array<String>) {
        if (sender is Player) {
            playerCommand(sender, args)
        } else {
            if (args.isEmpty()) {
                sender.sendMessage("/$name help")
            } else {
                sendUsage(sender)
            }
        }
    }

    var usagePrefix: String = "§cUtilize: "
    var description = "Este comando faz algo"
    var permission: String = ""
    var usage: String = ""
    var aliases: List<String> = ArrayList()
    var permissionMessage = Mine.MSG_NO_PERMISSION


    val command: PluginCommand
        get() = Bukkit.getPluginCommand(name)

    init {
        this.aliases = aliases.toList()
    }

    constructor() : this("") {}

    private fun autoUsage(): String {
        return if (parent != null) {
            parent?.autoUsage() + " " + name
        } else {
            "/$name"
        }
    }


    private fun autoPermission(): String {

        return if (parent != null) {
            parent?.autoPermission() + "." + name
        } else {
            "command.$name"
        }

    }


    fun broadcast(message: String) {
        Mine.broadcast(message, permission)

    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.isNotEmpty()){
            val arg = args[0]
            var sub: CommandManager? = null
            for (subcmd in subCommands.values) {
                if (subcmd.name.equals(arg, ignoreCase = true)) {
                    sub = subcmd
                }
                for (alias in subcmd.aliases) {
                    if (alias.equals(arg, ignoreCase = true)) {
                        sub = subcmd
                    }
                }
            }
            if (sub!=null){
                val args2 = args.copyOfRange(1, args.size)
                return sub.onCommand(sender,command,label,args2)
            }else{
                command(sender, args)
            }
        }else{
            if (sender.hasPermission(permission)) {
                command(sender, args)
            }else{
                sender.sendMessage(permissionMessage)
            }
        }
        return true;

    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        val vars = ArrayList<String>()
        var cmd = this
        for (index in args.indices) {
            val arg = args[index].toLowerCase()
            vars.clear()
            var sub: CommandManager? = null
            for (subcmd in cmd.subCommands.values) {
                if (!sender.hasPermission(subcmd.permission)) continue
                if (Extra.startWith(subcmd.name, arg)) {
                    vars.add(subcmd.name)
                }
                if (subcmd.name.equals(arg, ignoreCase = true)) {
                    sub = subcmd
                }
                for (alias in subcmd.aliases) {
                    if (Extra.startWith(alias, arg)) {
                        vars.add(alias)
                    }
                    if (alias.equals(arg, ignoreCase = true)) {
                        sub = subcmd
                    }
                }


            }
            if (sub == null) {
                break
            }
            cmd = sub
        }
        return if (vars.isEmpty()) {


            null
        } else vars
    }

    private fun fixPermissionAndUsage() {
        if (usage.isEmpty()) {
            usage = autoUsage()
        }
        if (permission.isEmpty()) {
            permission = autoPermission()
        }
    }

    fun register(): Boolean {
        fixPermissionAndUsage()
        val command = Bukkit.getPluginCommand(name)
        if (command == null) {
            log("O comando §a$name §fnao foi registrado na plugin.yml de nenhum Plugin do Servidor")
            return false
        }
        plugin = command.plugin as JavaPlugin
        if (command.usage != null) {
            if (command.usage.isNotEmpty()) {
                usage = command.usage.replace("<command>", name).replace('&', '§')
            }
        }

        if (command.permission != null) {
            permission = command.permission
        }
        if (command.permissionMessage != null) {
            permissionMessage = command.permissionMessage.formatColors()
        }
        if (command.description != null) {
            description = command.description.formatColors()
        }
        // alias não funciona para comandos apenas na plugin.yml ou subcomandos
        if (command.aliases != null) {
            aliases = command.aliases
        }

        command.usage = usage
        command.label = name
        command.aliases = aliases

        command.description = description
        command.permission = permission
        command.executor = this

        log(
            "O comando §a" + name + " §ffoi registrado para o Plugin §b" + command.plugin.name
                    + "§f pela plugin.yml"
        )
        commandsRegistred[name.toLowerCase()] = this
        updateSubs()
        registerListener(plugin)
        return true

    }

    fun register(sub: CommandManager): Boolean {
        subCommands[sub.name] = sub
        sub.parent = this
        return true
    }

    fun registerCommand(plugin: Plugin) {
        fixPermissionAndUsage()
        this.plugin = plugin as JavaPlugin
        val command = CustomCommand(this)
        command.aliases = aliases
        command.description = description
        command.label = name
        // command.setName(name);
        command.usage = usage
        command.permissionMessage = permissionMessage
        command.permission = permission
        log(
            "O comando §a" + name + " §ffoi registrado para o Plugin §b" + plugin.name
                    + "§f sem plugin.yml"
        )
        commandsRegistred[name.toLowerCase()] = this

        updateSubs()
        registerListener(plugin)
        Mine.createCommand(plugin, command)
    }

    fun unregisterCommand() {
        if (customCommand != null)
            Mine.removeCommand(name)

    }


    fun sendPermissionMessage(sender: CommandSender) {
        sender.sendMessage(permissionMessage)
    }

    fun sendUsage(sender: CommandSender) {

        sender.sendMessage(usagePrefix + usage)
    }


    private fun updateSubs() {
        for (sub in subCommands.values) {
            sub.parent = this
            log("O subcomando §e" + sub.name + " §ffoi registrado no comando §a" + name)
            if (sub.subCommands.isNotEmpty())
                sub.updateSubs()
        }
    }

    companion object {
        var isDebug = true
        val commandsRegistred: MutableMap<String, CommandManager> = HashMap()

        fun getCommand(name: String): CommandManager {
            return commandsRegistred[name.toLowerCase()]!!
        }

        fun log(msg: String) {

            if (isDebug)
                Mine.console("§bCommandAPI §f$msg")
        }


    }


    /**
     * Classe para o comando customizado
     */
    class CustomCommand(val command: CommandManager) : Command(command.name) {
        override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
            try {
                if (!command.plugin.isEnabled) {
                    return false
                }
                if (sender.hasPermission(permission)) {

                    return command.onCommand(sender, this, label, args)

                } else {
                    command.sendPermissionMessage(sender)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return false
        }

        @Throws(IllegalArgumentException::class)
        override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String>? {

            val vars = command.onTabComplete(sender, this, name, args)
            val superior = super.tabComplete(sender, alias, args)
            return vars ?: superior


        }
    }


}
