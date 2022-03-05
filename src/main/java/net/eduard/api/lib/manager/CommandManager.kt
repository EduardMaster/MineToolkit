package net.eduard.api.lib.manager

import net.eduard.api.lib.kotlin.formatColors
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

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
    var commandRegistred: Command? = null

    @Transient
    var subCommands: MutableMap<String, CommandManager> = mutableMapOf()

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

    var usagePrefix: String = DEFAULT_USAGE_PREFIX
    var description = DEFAULT_DESCRIPTION
    var permission: String = DEFAULT_PERMISSION
    var usage: String = ""
    var aliases: MutableList<String> = ArrayList()
    var permissionMessage = Mine.MSG_NO_PERMISSION


    val command: PluginCommand
        get() = Bukkit.getPluginCommand(name)

    init {
        this.aliases = aliases.toMutableList()
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
        Bukkit.broadcast(message, permission)
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.isNotEmpty()) {
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
            if (sub != null) {
                val args2 = args.copyOfRange(1, args.size)
                return sub.onCommand(sender, command, label, args2)
            } else {
                if (sender.hasPermission(permission)) {
                    command(sender, args)
                } else {
                    sender.sendMessage(permissionMessage)
                }
            }
        } else {
            if (sender.hasPermission(permission)) {
                command(sender, args)
            } else {
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
        var cmdSelected = this
        for (index in args.indices) {
            val arg = args[index].toLowerCase()
            vars.clear()
            var sub: CommandManager? = null
            for (subCMD in cmdSelected.subCommands.values) {
                if (!sender.hasPermission(subCMD.permission)) continue
                if (Extra.startWith(subCMD.name, arg)) {
                    vars.add(subCMD.name)
                }
                if (subCMD.name.equals(arg, ignoreCase = true)) {
                    sub = subCMD
                }
                for (alias in subCMD.aliases) {
                    if (Extra.startWith(alias, arg)) {
                        vars.add(alias)
                    }
                    if (alias.equals(arg, ignoreCase = true)) {
                        sub = subCMD
                    }
                }
            }
            if (sub == null) {
                break
            }
            cmdSelected = sub
        }
        if (cmdSelected != this) {
            val subTabComplete = cmdSelected.onTabComplete(sender, command, label, args)
            if (subTabComplete != null) {
                vars.addAll(subTabComplete)
            }
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
                usage = command.usage
                    .replace("<command>", name, false)
                    .replace('&', '§', false)
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
        command.setName(name);
        command.usage = usage
        command.permissionMessage = permissionMessage
        command.permission = permission
        log("O comando §a" + name + " §ffoi registrado para o Plugin §b" + plugin.name + "§f sem plugin.yml")

        commandsRegistred[name.toLowerCase()] = this
        commandRegistred = command
        updateSubs()
        registerListener(plugin)
        Bukkit.getScheduler().runTask(plugin) {
            try {
                val registrado = getServerCommandsMap()?.register(plugin.name, command) ?: false
                log("O Comando $name foi registrado com sucesso: $registrado")

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    }


    fun unregisterCommand() {
        val command =  commandRegistred ?: return
        val cmdName = command.name.toLowerCase()
        val pluginName = plugin.name.toLowerCase()
        commandsRegistred.remove(name.toLowerCase())
        commandsRegistred.remove(name)
        command.unregister(getServerCommandsMap())
        val commandsMap = getServerCommandsHashMap()!!
        for (aliase in aliases) {
            val aliasLowercase = aliase.toLowerCase()
            log("Removendo aliase §a$aliase§f do comando §b$cmdName")
            commandsMap.remove(aliasLowercase)
            commandsMap.remove("$pluginName:$aliasLowercase")
        }
        try {
            commandsMap.remove(cmdName)
            commandsMap.remove("$pluginName:$cmdName")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        log("Removendo o comando §a$cmdName§f do Plugin §b$pluginName")
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
        var debugEnabled = true
        var DEFAULT_USAGE_PREFIX = "§cUtilize: "
        var DEFAULT_DESCRIPTION = "Descrição do Comando"
        var DEFAULT_PERMISSION = ""
        val commandsRegistred: MutableMap<String, CommandManager> = HashMap()

        /**
         * @return O Hashmap onde estão registrados todos comandos e aliases
         */
        private fun getServerCommandsMap(): CommandMap? {
            try {

                return Extra.getFieldValue(Bukkit.getServer().pluginManager, "commandMap") as CommandMap
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }

        /**
         * @return O Hashmap onde estão registrados todos comandos e aliases
         */
        private fun getServerCommandsHashMap(): MutableMap<String, Command>? {
            try {
                val simpleCommandMap = getServerCommandsMap()
                val field = SimpleCommandMap::class.java.getDeclaredField("knownCommands")
                field.isAccessible = true
                return field.get(simpleCommandMap) as MutableMap<String, Command>
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }

        fun unregisterCommands(plugin: Plugin) {
            val commands = commandsRegistred.values.filter { it.plugin == plugin }
            commands.forEach {
                it.unregisterCommand()
            }
        }

        fun getCommand(name: String): CommandManager {
            return commandsRegistred[name.toLowerCase()]!!
        }

        fun log(message: String) {
            if (debugEnabled)
                Mine.console("§bCommandAPI §f$message")
        }


    }


    /**
     * Classe para registrar Comando sem a plugin.yml parecido com PluginCommand
     */
    class CustomCommand(val command: CommandManager) : Command(command.name) {
        override fun execute(sender: CommandSender, label: String, args: Array<String>): Boolean {
            try {
                if (!command.plugin.isEnabled) {
                    sender.sendMessage("§cPlugin ${command.plugin.name} foi desabilitado.")
                    return false
                }
                if (sender.hasPermission(permission)) {
                    return command.onCommand(sender, this, label, args)
                } else {
                    command.sendPermissionMessage(sender)
                }
            } catch (ex: Exception) {
                sender.sendMessage("§cUm Erro ocorreu ao executar este comando.")
                ex.printStackTrace()
            }

            return false
        }

        @Throws(IllegalArgumentException::class)
        override fun tabComplete(sender: CommandSender, alias: String, args: Array<String>): List<String>? {

            val tabCompletePossible = command.onTabComplete(sender, this, name, args)
            return tabCompletePossible ?: super.tabComplete(sender, alias, args)


        }
    }


}
