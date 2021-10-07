package net.eduard.api.command.map

import net.eduard.api.lib.kotlin.text
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class MapListCommand : CommandManager("list", "lsitar", "status") {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
       sender.sendMessage("§3Mapas carregados:")
        for (map in MinigameSchematic.schematics.values){
            sender.sendMessage("§bNome: "+map.name+" High: "+map.height.text
            + " Length: "+map.length.text + " Width: "+ map.width.text)
        }
        sender.sendMessage("§6Mapas no HD:")
        for (subFile in MinigameSchematic.MAPS_FOLDER.listFiles()){
            sender.sendMessage("§e"+subFile.name)
        }
        return true
    }

    init {
        description = "Mostra os comandos existentes"
    }
}