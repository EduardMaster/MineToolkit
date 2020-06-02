package net.eduard.api.server.minigame

import java.util.ArrayList
import java.util.Arrays

import net.eduard.api.lib.game.ItemRandom
import net.eduard.api.lib.modules.Extra
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.storage.Storable

class MinigameChest {
    var refilTime = 2 * 60
    var maxItems = 10
    var isAcumulateItems = true
    var isRandomItems = true
    var isShuffleItems = true
    var isNoRepeatItems: Boolean = false
    var items: MutableList<ItemRandom> = ArrayList()

    fun fill(inv: Inventory) {
        if (!isAcumulateItems) {
            inv.clear()
        }
        var index = 0
        while (index < maxItems) {
            var item: ItemStack? = null
            var itemrandom: ItemRandom? = null
            if (isRandomItems) {
                itemrandom = Extra.getRandom(items)
            } else {
                if (index < items.size) {
                    itemrandom = items[index]
                }
            }
            if (itemrandom != null) {
                item = itemrandom.createChance()
            }
            if (item!!.type == Material.AIR) {
                index--
            }
            inv.setItem(inv.firstEmpty(), item)
            index++
        }
        if (isNoRepeatItems) {
            var contents = inv.contents
            val stream = Arrays.stream(contents)
            stream.distinct()
            //contents = stream.toArray { size -> arrayOfNulls(size) }
            inv.contents = stream.toArray() as Array<ItemStack?>
        }
        if (isShuffleItems) {
            val contents = inv.contents
            for (i in contents.indices) {
                val itemStack = contents[i]
                var temp = Mine.getRandomInt(1, contents.size)
                temp--
                val tempItem = contents[temp]
                contents[i] = tempItem
                contents[temp] = itemStack
            }
            inv.contents = contents
        }

    }

}
