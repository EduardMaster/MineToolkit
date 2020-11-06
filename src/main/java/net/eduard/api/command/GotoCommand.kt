package net.eduard.api.command

import net.eduard.api.EduardAPI.Companion.OPT_SOUND_TELEPORT
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Mine

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GotoCommand : CommandManager("goto") {
    var message = "§6Voce foi teleportado para o Mundo §e\$world"
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) return false
        if (Mine.onlyPlayer(sender)) {
            val p = sender as Player
            if (Mine.existsWorld(sender, args[0])) {
                val world = Bukkit.getWorld(args[0])
                Mine.teleport(p, world.spawnLocation)
                OPT_SOUND_TELEPORT.create(p)
                Mine.send(p, message.replace("\$world", world.name))
            }
        }
        return true
    }
}