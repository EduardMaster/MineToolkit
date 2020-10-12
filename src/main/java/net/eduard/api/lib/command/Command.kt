package net.eduard.api.lib.command

import net.eduard.api.lib.modules.Extra
import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Error
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

    var usage = ""

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

    override var permission = ""
    override var permissionMessage = MESSAGE_PERMISSION
    override var playerOnly = false

    open fun onCommand(sender: Sender, args: List<String>) {
        if (sender is PlayerOnline<*>) {
            onCommand(sender, args)
        }

    }

    fun processTabComplete(sender: Sender, args: List<String>): List<String> {
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

    fun processCommand(sender: Sender, args: List<String>) {
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
        } else
            sender.sendMessage(cmd.permissionMessage)

    }

    fun sendUsage(sender: Sender) {
        sender.sendMessage(usage)
    }

    open fun onCommand(sender: PlayerOnline<*>, args: List<String>) {
        sendUsage(sender)
    }

    fun register(subCommand: Command) {
        subCommands.add(subCommand)
        subCommand.parent = this
    }

    fun register(main: Any) {

        if (usage.isEmpty()){
            usage = autoUsage()
        }
        if (permission.isEmpty()){
            permission = autoPermission()
        }


        try {
            if (main is JavaPlugin) {
                BukkitCommand(this).register(main)

            }
        } catch (er: Error) {
        }


        try {
            if (main is Plugin) {

                BungeeCommand(this).register(main)
            }
        } catch (er: Error) {
        }
        ConsoleSender.sendMessage("Comando $name registrado para o plugin $main")

        COMMANDS.add(this)

    }

}