package net.eduard.api.command.map

import net.eduard.api.lib.kotlin.text
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import java.nio.file.Files

class MapListCommand : CommandManager("list", "lsitar", "status") {
    override fun command(sender: CommandSender, args: Array<String>) {
        sender.sendMessage("§3Mapas carregados:")
        for (map in MinigameSchematic.getSchematics()){
            sender.sendMessage("§bNome: "+map.name+" High: "+map.height.text
                    + " Length: "+map.length.text + " Width: "+ map.width.text)
        }
        sender.sendMessage("§6Mapas no HD:")
        for (subFile in MinigameSchematic.MAPS_FOLDER.listFiles()!!){
            val spaceUsed = Files.size(subFile.toPath())
            sender.sendMessage("§e"+subFile.name+" §f"+((spaceUsed).text)+" bytes")
        }
    }

    init {
        description = "Mostra os comandos existentes"
    }
}