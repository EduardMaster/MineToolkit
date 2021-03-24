package net.eduard.api.command

import net.eduard.api.lib.kotlin.formatColors
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import java.lang.Exception

class RunCommand : CommandManager("run", "rodar") {

    init {
        usage = "/run <method-name> <double>"
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        if (args.isEmpty()) {
            sendUsage(sender)
        } else {
            if (sender is Player) {
                val cmd = args[0]
                var type: Class<*> = Void::class.java
                if (args.size == 1) {
                    try {
                        val method = Extra.getMethod(Player::class.java, cmd)
                        sender.sendMessage("§aInfo: §f" + method.invoke(sender))

                    } catch (ex: Exception) {
                        sender.sendMessage("§cComando Feito errado")
                        sender.sendMessage("§eDetalhes do Erro")
                        sender.sendMessage("§aCause: " + ex.cause)
                        sender.sendMessage("§bLocalized: " + ex.localizedMessage)
                        sender.sendMessage("§6Message: " + ex.message)
                        sender.sendMessage("§0---------------------------------")
                    }

                } else {
                    var text = args[1]
                    val value: Any
                    if (text.endsWith("f")) {
                        text = text.replaceFirst("f", "")
                        value = Extra.toFloat(text)
                        type = Float::class.java

                    } else if (text.startsWith("'")) {
                        value = Extra.getText(1, *args).replaceFirst("'", "").formatColors()
                        type = String::class.java
                    } else if (text == "true" || text == "false") {
                        value = Extra.toBoolean(text)
                        type = Boolean::class.java
                    } else if (!text.contains(".")) {
                        value = Extra.toInt(text)
                        type = Int::class.java
                    } else {
                        value = Extra.toDouble(text)
                        type = Double::class.java
                    }
                    try {

                        if (type == Void::class.java) {
                            val method = Extra.getMethod(Player::class.java, cmd)
                            sender.sendMessage("Info: " + method.invoke(sender))
                        } else {
                            val method = Extra.getMethod(Player::class.java, cmd, type)
                            method.invoke(sender, value)
                        }

                        sender.sendMessage("§aComando feito com sucesso")
                    } catch (ex: Exception) {
                        sender.sendMessage("§cComando Feito errado")
                        sender.sendMessage("§eDetalhes do Erro")
                        sender.sendMessage("§aCause: " + ex.cause)
                        sender.sendMessage("§bLocalized: " + ex.localizedMessage)
                        sender.sendMessage("§6Message: " + ex.message)
                        sender.sendMessage("§0---------------------------------")
                    }
                }
            }
        }

        return true
    }
}