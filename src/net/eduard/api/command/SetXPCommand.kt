package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetXPCommand : CommandManager("setexperience") {
    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val p = sender
            if (args.isEmpty()) {
                return false
            } else {
                var amount = Extra.fromMoneyToDouble(args[0])
                p.totalExperience = 0
                p.exp = 0f
                p.level = 0
                if (amount > Int.MAX_VALUE) {
                    amount = Int.MAX_VALUE.toDouble()
                }
                p.giveExp(amount.toInt())
                p.sendMessage("§aSua xp foi alterada para: §2$amount")
                p.sendMessage("§aSeu novo nível é: §2" + p.level)
                p.sendMessage("§aSua barra de XP: §2" + p.exp)
            }
        }
        return true
    }
}