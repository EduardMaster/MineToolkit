package net.eduard.api.command

import net.eduard.api.lib.kotlin.displayEnchants
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import java.util.*

class EnchantCommand : CommandManager("enchantment", "ench") {
    init {
        description = "Encanta o item para um nivel"
        usage = "/<command> <enchant> <level>"
    }

    var messageInvalid = "§cDigite o encantamento valido! §bAperte TAB"
    var message = "§aEncantamento aplicado!"
    var messageError = "§cVoce precisar ter um item em maos!"
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        return if (args.size == 1) {
            getEnchants(args[0])
        } else null
    }

    override fun playerCommand(player: Player, args: Array<String>) {
        if (args.isEmpty()) {
            return
        }
        if (player.itemInHand == null || player.itemInHand.type == Material.AIR) {
            player.sendMessage(messageError)
            return
        }
        val enchant = Mine.parseEnchant(args[0])
        if (enchant == null) {
            player.sendMessage(messageInvalid)
        } else {
            var nivel = 1
            if (args.size >= 2) {
                nivel = Extra.toInt(args[1])
            }
            if (nivel == 0) {
                player.itemInHand.removeEnchantment(enchant)
            } else
                player.itemInHand.addUnsafeEnchantment(enchant, nivel)
            player.itemInHand.displayEnchants()
            player.sendMessage(message)
        }
    }


    companion object {
        fun getEnchants(arg: String?): List<String> {
            var argument = arg ?: ""
            argument = argument.trim { it <= ' ' }.replace("_", "")
            val list: MutableList<String> = ArrayList()
            for (enchant in Enchantment.values()) {
                val text = Extra.toTitle(enchant.name, "")
                val line = enchant.name.trim { it <= ' ' }.replace("_", "")
                if (Extra.startWith(line, argument)) {
                    list.add(text)
                }
            }
            return list
        }
    }
}