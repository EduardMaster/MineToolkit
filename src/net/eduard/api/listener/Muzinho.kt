package net.eduard.api.listener

import net.eduard.api.lib.menu.Menu
import net.eduard.api.lib.modules.Mine
import org.bukkit.Material

class Muzinho : Menu("MenuZinho",3)
{
    init{
        isAutoAlignItems= true
        autoAlignSkipCenter = true
        openWithCommand= "/teste"
        repeat(200){n->
            button("button$n") {
                item = Mine.newItem(Material.STONE,"§aItem §b$n")
            }
        }
    }

}