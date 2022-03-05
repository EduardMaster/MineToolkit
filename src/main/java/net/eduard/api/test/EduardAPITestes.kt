package net.eduard.api.test

import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.kotlin.player
import net.eduard.api.lib.manager.CommandManager
import net.eduard.api.lib.menu.ClickEffect
import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.plugin.IPluginInstance
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin

fun IPluginInstance.enableTests() {
    MenuTest().registerMenu(this)
    CommandTest().registerCommand(plugin as Plugin)
}
class CommandTest() : CommandManager("commandtest"){
    override fun command(sender: CommandSender, args: Array<String>) {
        sender.sendMessage("§aEste eh um comando criado para puro teste.")
    }
}

class MenuTest : Menu("Teste de Menu", 3) {
    init {
        openWithCommand = "/menutest"
        button("novo-botao") {
            setPosition(5,2)
            icon = ItemBuilder(Material.DIAMOND_SWORD).name("§bMenu §3Exemplo")
                .lore("§7","§7Lore Exemplo")
            click = ClickEffect {
                val player = it.player
                player.sendMessage("§aVocê clicou no Item")
            }
        }
    }

}