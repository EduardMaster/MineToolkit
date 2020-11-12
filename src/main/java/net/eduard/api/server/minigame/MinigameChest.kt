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
    var maxItems = 6
    var canAcumulate = false
    var needShuffle = true
    var canRepeat = false
    var items = mutableListOf<ItemRandom>()

    fun fill(inv: Inventory) {
        val list = items.toMutableList()
        if (!canAcumulate) {
            inv.clear()
        }
        if (items.isEmpty()){
            Mine.console("§cMinigamechest is Empty not filling chests")
            return
        }

        for (itemsSorted in 0..maxItems) {
            val itemRandom = list.randomByPercent { chance }
            val item = itemRandom.createRandom()
            if (!canRepeat){
                list.remove(itemRandom)
            }
            inv.setItem(inv.firstEmpty(), item)
            if (list.isEmpty()){
                break
            }
        }

        if (needShuffle) {
            inv.contents.shuffle()
        }
    }

}
