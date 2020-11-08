package net.eduard.api.server.minigame


import net.eduard.api.lib.game.ItemRandom
import net.eduard.api.lib.kotlin.randomByPercent
import net.eduard.api.lib.kotlin.shuffle
import net.eduard.api.lib.modules.Mine

import org.bukkit.inventory.Inventory

/**
 * Representa um tipo de Bau do minigame
 */
class MinigameChest {
    var refilTime = 2 * 60
    var maxItems = 10
    var isAcumulateItems = false
    var isShuffleItems = true
    var isNoRepeatItems = true
    var items = mutableListOf<ItemRandom>()

    fun fill(inv: Inventory) {
        if (!isAcumulateItems) {
            inv.clear()
        }
        if (items.isEmpty()){
            Mine.console("§cMinigamechest is Empty not filling chests")
            return
        }

        for (itemsSorted in 0..maxItems) {
            val itemRandom = items.randomByPercent { chance }
            val item = itemRandom.createRandom()
            inv.setItem(inv.firstEmpty(), item)
        }
        if (isNoRepeatItems) {
            inv.contents = inv.contents.distinct().toTypedArray()
        }
        if (isShuffleItems) {
            inv.contents.shuffle()
        }
    }

}
