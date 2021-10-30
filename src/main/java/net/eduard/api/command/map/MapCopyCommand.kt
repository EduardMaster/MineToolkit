package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapCopyCommand : CommandManager("copy", "copiar") {

    override fun playerCommand(player: Player, args: Array<String>) {
        val minigameSchematic  =
            MinigameSchematic.getSchematic(player)
        minigameSchematic.copy(player.location)
        player.sendMessage("§bEduardAPI §aMapa copiado!")
    }


    init {
        description = "Copia os blocos da posição 1 a posção 2"
    }
}