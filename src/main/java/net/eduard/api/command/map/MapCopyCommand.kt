package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.GameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapCopyCommand : CommandManager("copy", "copiar") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        if (Mine.onlyPlayer(sender)) {
            val player = sender as Player
            val schema  =
                GameSchematic.getSchematic(player)
            schema.copy(player.location)
            player.sendMessage("§bEduardAPI §aMapa copiado!")
        }
        return true
    }

    init {
        description = "Copia os blocos da posição 1 a posção 2"
    }
}