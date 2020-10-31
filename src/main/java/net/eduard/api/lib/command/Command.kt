package net.eduard.api.lib.command

import net.eduard.api.lib.hybrid.Hybrid
import net.eduard.api.lib.hybrid.IPlayer
import net.eduard.api.lib.hybrid.ISender
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.plugin.IPluginInstance
import java.util.ArrayList

open class Command(override var name: String = "comando", vararg aliases: String) : ICommand {


    companion object {
        var MESSAGE_PERMISSION = "§cVocê não tem permissão para executar este comando."
        val COMMANDS = mutableListOf<Command>()

    }

    fun List<String>.sendNoMessage(arg: Int): Boolean {
        if (size <= arg) return false
        return get(arg) == "-msg"
    }

    @Transient
    var parent: Command? = null

    final override var aliases = mutableListOf<String>()
    override var description = "Descrição do comando"

    override var usage = ""
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
            "/$name help"
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
        for (i in args.indices) {
            val arg = args[i].toLowerCase()
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
        for (i in args.indices) {
            val arg = args[i].toLowerCase()
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
            sender.sendMessage("§cVocê precisa da permissão " + cmd.permission)
            sender.sendMessage(cmd.permissionMessage)
        }

    }

    fun sendUsage(sender: ISender) {
        sender.sendMessage(usage)
    }


    fun register(subCommand: Command) {
        subCommands.add(subCommand)
        subCommand.parent = this
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
            BungeeCommand(this).register(main)
        } else {
            BukkitCommand(this).register(main)
        }
        Hybrid.instance.console.sendMessage("Comando $name registrado para o plugin $main")

        COMMANDS.add(this)

    }

}