package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetXPCommand : CommandManager("setexperience","setexp","setxp") {

    init{
        description= "Defina a experiencia (EXP) do jogador"
        usage= "/<command> <xp>"
    }

    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
            return
        }
        var amount = Extra.fromMoneyToDouble(args[0])
        player.totalExperience = 0
        player.exp = 0f
        player.level = 0
        if (amount > Int.MAX_VALUE) {
            amount = Int.MAX_VALUE.toDouble()
        }
        player.giveExp(amount.toInt())
        player.sendMessage("§bSua xp foi alterada para: §3$amount")
        player.sendMessage("§bSeu novo nível é: §3" + player.level)
        player.sendMessage("§bSua barra de XP: §3" + player.exp)
    }

}