package net.eduard.api.server.minigame

import net.eduard.api.lib.game.ItemRandom
import net.eduard.api.lib.kotlin.randomByPercent
import org.bukkit.inventory.ItemStack

class MinigameItemsGroup{
    var maxItems = 1
    var items = mutableListOf<ItemRandom>()

    fun add(item : ItemRandom){
        items.add(item)
    }
    fun clear(){
       items.clear()
    }
    fun randomize() : List<ItemStack>{
        val finalList = mutableListOf<ItemStack>()
        var sorted = 0
        val itemsUsed = items.toMutableList()
        while(sorted< maxItems){
            if (itemsUsed.isEmpty())break
            val itemRandom = itemsUsed.randomByPercent { this.chance }
            val item = itemRandom.createRandom()
            if (itemRandom.maxRepeats == itemRandom.repeats){
                itemRandom.repeats=0
                itemsUsed.remove(itemRandom)
            }
            finalList.add(item)
            sorted++
        }
        return finalList
    }
}