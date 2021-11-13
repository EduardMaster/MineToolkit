package net.eduard.api.lib.command

import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.hybrid.IPlayer
import net.eduard.api.lib.hybrid.ISender
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.plugin.IPluginInstance
import java.util.ArrayList

open class Command(
    override var name: String = "comando",
    vararg aliases: String
) : ICommand {


    companion object {
        var MESSAGE_PERMISSION = "§cVocê não possui permissão para executar este comando. §f%permission"
        val COMMANDS = mutableListOf<Command>()

        fun unregisterCommands(plugin: IPluginInstance) {
            val commandsToUnregister = COMMANDS.filter { plugin == it.plugin }
            commandsToUnregister.forEach {
                it.unregister()
            }
        }

    }

    fun List<String>.sendNoMessage(arg: Int): Boolean {
        if (size <= arg) return false
        return get(arg) == "-msg"
    }

    @Transient
    var parent: Command? = null

    @Transient
    var plugin: IPluginInstance? = null

    @Transient
    var command: HybridCommand? = null

    final override var aliases = mutableListOf<String>()
    override var description = "Descrição do comando"

    override var usage = ""
    var prefixUsage = "§cUtilize: "
    override var permission = ""

    @Transient
    override var subCommands = mutableListOf<Command>()

    init {
        this.aliases.addAll(aliases)
    }

    private fun autoPermission(): String {

        return if (parent != null) {
            parent?.autoPermission() + "." + name
        } else {
            "command.$name"
        }

    }

    private fun autoUsage(): String {
        return if (parent != null) {
            parent?.autoUsage() + " " + name
        } else {
            "/$name "
        }
    }


    override var permissionMessage = MESSAGE_PERMISSION
    override var playerOnly = false

    open fun onCommand(sender: IPlayer<*>, args: List<String>) {
        sendUsage(sender)

    }

    open fun onCommand(sender: ISender, args: List<String>) {
        if (sender is IPlayer<*>) {
            onCommand(sender, args)
        } else
            sendUsage(sender)
    }

    fun processTabComplete(sender: ISender, args: List<String>): List<String> {
        val vars = ArrayList<String>()
        var cmd = this
        for (index in args.indices) {
            val arg = args[index].toLowerCase()
            vars.clear()
            var sub: Command? = null
            for (subcmd in cmd.subCommands) {
                if (sender.hasPermission(subcmd.permission)) {
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

            }
            if (sub == null) {
                break
            }
            cmd = sub
        }
        return vars
    }

    fun processCommand(sender: ISender, args: List<String>) {
        var cmd = this
        for (index in args.indices) {
            val arg = args[index].toLowerCase()
            var sub: Command? = null
            for (subcmd in cmd.subCommands) {
                if (subcmd.name.equals(arg, ignoreCase = true)) {
                    sub = subcmd
                }
                for (alias in subcmd.aliases) {
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


        if (sender.hasPermission(cmd.permission)) {
            cmd.onCommand(sender, args)
        } else {
            sender.sendMessage(
                cmd.permissionMessage
                    .replace("%permission", cmd.permission, false)
            )
        }

    }

    open fun sendUsage(sender: ISender) {
        sender.sendMessage(prefixUsage + usage)

    }


    fun register(subCommand: Command) {
        subCommands.add(subCommand)
        subCommand.parent = this
    }

    fun unregister() {
        val plugin = plugin!!
        command?.unregister(plugin)
        Hybrid.instance.console.sendMessage("§fComando §a$name §fdesregistrado para o plugin §e${plugin.systemName}")
        COMMANDS.remove(this)
    }

    fun register(main: IPluginInstance) {

        if (usage.isEmpty()) {
            usage = autoUsage()
        }
        if (permission.isEmpty()) {
            permission = autoPermission()
        }
        for (sub in subCommands) {
            if (sub.usage.isEmpty()) {
                sub.usage = sub.autoUsage()
            }
            if (sub.permission.isEmpty()) {
                sub.permission = sub.autoPermission()
            }
        }

        if (Hybrid.instance.isBungeecord) {
            command = BungeeCommand(this)
            command?.register(main)
        } else {
            command = BukkitCommand(this)
            command?.register(main)
        }
        Hybrid.instance.console.sendMessage("§fComando §a$name §fregistrado para o plugin §e${main.systemName}")
        plugin = main
        COMMANDS.add(this)

    }

}