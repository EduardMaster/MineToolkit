package net.eduard.api.command

import net.eduard.api.core.PlayerSkin
import net.eduard.api.lib.manager.CommandManager
import org.bukkit.entity.Player

class SetSkinCommand : CommandManager("setskin","setplayerskin") {
    init{
        description= "Defina uma Skin para si mesmo"
        usage= "/<command> <playerName>"
    }
    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
            return
        }
        val playerName = args[0]
        player.sendMessage("§aSua skin foi alterada para $playerName")
        PlayerSkin.change(player, playerName)
    }

}