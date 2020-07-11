package net.eduard.api.lib.command

import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Error
import java.lang.Exception

open class Command(override var name: String = "comando", vararg aliases: String) : ICommand {


    companion object {
        var MESSAGE_PERMISSION = "§cVocê não tem permissão para executar este comando."
        val COMMANDS = mutableListOf<Command>()

    }

    @Transient
    var parent: Command? = null

    override var aliases = mutableListOf<String>()
    override var description = "Descrição do comando"

    var usage = "§cDigite /comando"

    @Transient
    override var subCommands = mutableListOf<Command>()

    init {
        this.aliases.addAll(aliases)
    }

    fun autoPermission(): String {

        return if (parent != null) {
            parent?.autoPermission() + "." + name
        } else {
            "command.$name"
        }

    }

    fun autoUsage(): String {
        return if (parent != null) {
            parent?.autoUsage() + " " + name
        } else {

            "/$name"
        }
    }

    override var permission = "permissao.do.comando"
    override var permissionMessage = MESSAGE_PERMISSION
    override var playerOnly = false

    open fun onCommand(sender: Sender, args: List<String>) {
        if (sender is PlayerSender<*>) {
            onCommand(sender, args)
        }

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

        if (cmd === this) {
            if (args.isEmpty()) {
                sender.sendMessage("/$name help")
            } else {
                sendUsage(sender)
            }
        } else {

            if (sender.hasPermission(cmd.permission)) {
                cmd.onCommand(sender, args)
            } else
                sender.sendMessage(cmd.permissionMessage)
        }

    }

    fun sendUsage(sender: Sender) {
        sender.sendMessage(usage)
    }

    open fun onCommand(sender: PlayerSender<*>, args: List<String>) {}

    fun register(subCommand: Command) {
        subCommands.add(subCommand)
        subCommand.parent = this
    }

    fun register(main: Any) {


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
        println("Comando $name registrado para o plugin $main")

        COMMANDS.add(this)

    }

}