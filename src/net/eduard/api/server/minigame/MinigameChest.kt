package net.eduard.api.server.minigame


import net.eduard.api.lib.game.ItemRandom
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


class MinigameChest {
    var refilTime = 2 * 60
    var maxItems = 10
    var isAcumulateItems = false
    var isRandomItems = true
    var isShuffleItems = true
    var isNoRepeatItems: Boolean = true
    var items =  mutableListOf<ItemRandom>()

    fun fill(inv: Inventory) {
        if (!isAcumulateItems) {
            inv.clear()
        }
        if (items.isEmpty()){
            Mine.console("§cMinigamechest is Empty not filling chests")
            return
        }
        var itemsSorted = 0
        while (itemsSorted < maxItems) {
            var item: ItemStack? = null
            var itemrandom: ItemRandom? = null
            if (isRandomItems) {
                itemrandom = Extra.getRandom(items)
            } else {
                if (itemsSorted < items.size) {
                    itemrandom = items[itemsSorted]
                }
            }
            if (itemrandom != null) {
                item = itemrandom.create()
            }
            if (item!!.type == Material.AIR) {
                itemsSorted--
            }
            inv.setItem(inv.firstEmpty(), item)
            itemsSorted++
        }
        if (isNoRepeatItems) {
            inv.contents = inv.contents.distinct().toTypedArray()
        }
        if (isShuffleItems) {
            val contents = inv.contents
            for (i in contents.indices) {
                val itemStack = contents[i]
                var temp = Extra.getRandomInt(1, contents.size)
                temp--
                val tempItem = contents[temp]
                contents[i] = tempItem
                contents[temp] = itemStack
            }
            inv.contents = contents
        }

    }

}
