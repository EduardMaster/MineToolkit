package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.server.minigame.MinigameSchematic
import net.eduard.api.server.minigame.MinigameSchematic.Companion.isEditing
import org.bukkit.entity.Player
import java.io.File

class MapSaveCommand : CommandManager("save", "salvar") {
    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            sendUsage(player)
            return
        }

        if (!isEditing(player)) {
            player.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy")
            return
        }
        val schema = MinigameSchematic.getSchematic(player)
        schema.name = args[0].toLowerCase()
        schema.register()
        schema.save(File(MinigameSchematic.MAPS_FOLDER, schema.name + ".map"))

        player.sendMessage("§bEduardAPI §aMapa salvado com sucesso!")

    }

    init {
        usage = "/map save <name>"
        description = "Salva o Schematic (Mapa) copiado"
    }
}