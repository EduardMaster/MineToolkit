package net.eduard.api.lib.menu

import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

interface PagedMenu {

    fun open(player: Player, page: Int): Inventory

    fun open(player: Player): Inventory {
        return open(player, 1)
    }

    fun getPageOpen(player: Player): Int
    fun isOpen(player: Player): Boolean


}
