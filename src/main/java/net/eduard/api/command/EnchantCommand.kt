package net.eduard.api.command

import net.eduard.api.lib.manager.CommandManager
import lib.modules.Extra
import lib.modules.Mine
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import java.util.*

class EnchantCommand : CommandManager("enchantment") {
    var messageInvalid = "§cDigite o encantamento valido! §bAperte TAB"
    var message = "§aEncantamento aplicado!"
    var messageError = "§cVoce precisar ter um item em maos!"
    override fun onTabComplete(sender: CommandSender, command: Command,
                               label: String, args: Array<String>): List<String>? {
        return if (args.size == 1) {
            getEnchants(args[0])
        } else null
    }

    override fun onCommand(sender: CommandSender, command: Command,
                           label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val p = sender
            if (args.size == 0) {
                return false
            } else {
                if (p.itemInHand == null
                        || p.itemInHand.type == Material.AIR) {
                    p.sendMessage(messageError)
                    return true
                }
                val enchant = lib.modules.Mine.getEnchant(args[0])
                if (enchant == null) {
                    p.sendMessage(messageInvalid)
                } else {
                    var nivel = 1
                    if (args.size >= 2) {
                        nivel = lib.modules.Extra.toInt(args[1])
                    }
                    if (nivel == 0) {
                        p.itemInHand.removeEnchantment(enchant)
                    } else p.itemInHand.addUnsafeEnchantment(enchant, nivel)
                    p.sendMessage(message)
                }
            }
        }
        return true
    }

    companion object {
        fun getEnchants(arg: String?): List<String> {
            var argument = arg?: ""
            argument = argument.trim { it <= ' ' }.replace("_", "")
            val list: MutableList<String> = ArrayList()
            for (enchant in Enchantment.values()) {
                val text = lib.modules.Extra.toTitle(enchant.name, "")
                val line = enchant.name.trim { it <= ' ' }.replace("_", "")
                if (lib.modules.Extra.startWith(line, argument)) {
                    list.add(text)
                }
            }
            return list
        }
    }
}