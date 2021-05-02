package net.eduard.api.command

import net.eduard.api.core.PlayerSkin
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetSkinCommand : CommandManager("setskin") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
        } else {
            if (Mine.onlyPlayer(sender)) {
                val player = sender as Player
                val playerName = args[0]
                sender.sendMessage("§aSua skin foi alterada para $playerName")
                PlayerSkin.change(player, playerName)
                
            }
        }
        return true
    }
}