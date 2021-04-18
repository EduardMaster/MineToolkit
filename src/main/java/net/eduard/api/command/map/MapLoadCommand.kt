package net.eduard.api.command.map

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapLoadCommand : CommandManager("load", "carregar") {
    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§c/map load <name>")
        } else {
            if (Mine.onlyPlayer(sender)) {
                val player = sender as Player
                val name = args[0].toLowerCase()
                if (MinigameSchematic.exists(name)) {
                    MinigameSchematic.loadToCache(player, name)
                    player.sendMessage("§bEduardAPI §aMapa carregado com sucesso!")
                } else {
                    player.sendMessage("§bEduardAPI §aMapa invalido: §2" + args[1])
                }
            }
        }
        return true
    }

    init {
        description = "Carrega um mundo descarregado"
    }
}