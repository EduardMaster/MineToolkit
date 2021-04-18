package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapPasteCommand : CommandManager("paste", "colar") {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (!Mine.onlyPlayer(sender)) return true
        val player = sender as Player
        if (!MinigameSchematic.isEditing(player)) {
            player.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy")
            return true
        }
        val map: MinigameSchematic =
            MinigameSchematic.getSchematic(player)
        map.paste(player.location, false)
        player.sendMessage(
            "§bEduardAPI §aMapa colado com sucesso! §2(\$blocks)"
                .replace("\$blocks", "" + map.count)
        )

        return true
    }

    init {
        description = "Cola o Schematic (Mapa) no local que estiver"
    }
}