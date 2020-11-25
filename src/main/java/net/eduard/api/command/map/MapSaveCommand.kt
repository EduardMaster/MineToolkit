package net.eduard.api.command.map

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.GameSchematic
import net.eduard.api.server.minigame.GameSchematic.Companion.isEditing
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
        if (args.size == 1) {
            sendUsage(sender)
        } else {
            if (Mine.onlyPlayer(sender)) {
                val player = sender as Player
                if (!isEditing(player)) {
                    player.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy")
                    return true
                }
                val schema = GameSchematic.getSchematic(player)
                schema.name = args[1].toLowerCase()
                schema.register()
                schema.save(File(EduardAPI.MAPS_FOLDER,schema.name+".map"))

                player.sendMessage("§bEduardAPI §aMapa salvado com sucesso!")
            }
        }
        return true
    }

    init {
        usage = "/map save <name>"
        description = "Salva o Schematic (Mapa) copiado"
    }
}