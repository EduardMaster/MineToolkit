package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.GameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapPos2Command : CommandManager("pos2", "sethigh", "setpos2") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        if (Mine.onlyPlayer(sender)) {
            val p = sender as Player
            val schema =
                GameSchematic.getSchematic(p)
            schema.high = p.location.toVector()
            p.sendMessage("§bEduardAPI §aPosicão 2 setada!")
        }
        return true
    }

    init {
        description = "Define a posição 2"
    }
}