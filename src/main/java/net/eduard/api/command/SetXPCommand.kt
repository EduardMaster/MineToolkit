package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetXPCommand : CommandManager("setexperience") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        if (sender !is Player) return true
        val player = sender
        if (args.isEmpty()) {
            return true
        }
        var amount = Extra.fromMoneyToDouble(args[0])
        player.totalExperience = 0
        player.exp = 0f
        player.level = 0
        if (amount > Int.MAX_VALUE) {
            amount = Int.MAX_VALUE.toDouble()
        }
        player.giveExp(amount.toInt())
        player.sendMessage("§aSua xp foi alterada para: §2$amount")
        player.sendMessage("§aSeu novo nível é: §2" + player.level)
        player.sendMessage("§aSua barra de XP: §2" + player.exp)


        return true
    }
}