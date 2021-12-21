package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

class SoundCommand : CommandManager("sound","som") {

    init{
        description= "Toca um som do minecraft"
        usage= "/<command> <sound> [volume] [pitch]"
    }

    fun parseSound(textInput: String): List<String> {
        var argument = textInput
        argument = argument.trim { it <= ' ' }
            .replace("_", "")
        val list: MutableList<String> = ArrayList()
        for (enchant in Sound.values()) {
            val text = Extra.toTitle(enchant.name, "")
            val line = enchant.name.trim { it <= ' ' }.replace("_", "")
            if (Extra.startWith(line, argument)) {
                list.add(text)
            }
        }
        return list
    }

    override fun onCommand(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): Boolean {
        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }
        val arg = args[0]
        var sound = Mine.getSound(arg)
        if (sound == null) {
            sound = Sound.LEVEL_UP
        }
        var volume = 2f
        var pitch = 1f
        if (args.size >= 2) {
            volume = Extra.toFloat(args[1])
        }
        if (args.size >= 3) {
            pitch = Extra.toFloat(args[2])
        }
        var player: Player? = null
        if (sender is Player) {
            player = sender
        }
        if (args.size >= 4) {
            player = if (Mine.existsPlayer(sender, args[3])) {
                Mine.getPlayer(args[3])
            } else return true
        }
        if (player == null) {
            sender.sendMessage(Mine.MSG_PLAYER_NOT_EXISTS)
            return true
        }
        val world = false
        val location = player.location
        if (world) {
            player.world.playSound(location, sound, volume, pitch)
        } else player.playSound(location, sound, volume, pitch)
        sender.sendMessage("§aEfeito sonoro criado!")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender, command: Command,
        label: String, args: Array<String>
    ): List<String>? {
        return if (args.size == 1) {
            parseSound(args[0])
        } else null
    }

}