package net.eduard.api.command.map

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.MinigameSchematic
import net.eduard.api.server.minigame.MinigameSchematic.Companion.isEditing
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

class MapSaveCommand : CommandManager("save", "salvar") {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        if (Mine.onlyPlayer(sender)) {
            val player = sender as Player
            if (!isEditing(player)) {
                player.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy")
                return true
            }
            val schema = MinigameSchematic.getSchematic(player)
            schema.name = args[0].toLowerCase()
            schema.register()
            schema.save(File(MinigameSchematic.MAPS_FOLDER, schema.name + ".map"))

            player.sendMessage("§bEduardAPI §aMapa salvado com sucesso!")
        }

        return true
    }

    init {
        usage = "/map save <name>"
        description = "Salva o Schematic (Mapa) copiado"
    }
}