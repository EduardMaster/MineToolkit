package net.eduard.api.server.minigame


import net.eduard.api.lib.game.ItemRandom
import net.eduard.api.lib.kotlin.format
import net.eduard.api.lib.kotlin.shuffle
import net.eduard.api.lib.modules.Mine
import org.bukkit.Material

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Representa um tipo de Bau do minigame
 */
class MinigameChest {


    fun randomize(): List<ItemStack> {
        val finalList = mutableListOf<ItemStack>()
        var sorted = 0
        for (group in groups) {
            for (itemSorted in group.randomize()) {
                finalList.add(itemSorted)
                sorted++
                if (sorted >= maxItems) {
                    return finalList
                }
            }
        }
        for (item in items) {
            val itemSorted = item.create()
            item.repeats = 0
            if (itemSorted.type != Material.AIR) {
                finalList.add(itemSorted)
                sorted++
                if (sorted >= maxItems) {
                    return finalList
                }
            }

        }

        return finalList
    }


    var refilTime = 2 * 60
    var maxItems = 6
    var canAcumulate = false
    var needShuffle = true
    var items = mutableListOf<ItemRandom>()
    var itemsPerChest = mutableListOf<ItemRandom>()
    var groups = mutableListOf<MinigameItemsGroup>()


    inline fun group(setup: MinigameItemsGroup.() -> Unit): MinigameItemsGroup {
        val group = MinigameItemsGroup()
        setup.invoke(group)
        groups.add(group)
        return group
    }


    fun ItemStack.isHelmet(): Boolean {
        return type.name.contains("HELMET")
    }

    fun ItemStack.isChestplate(): Boolean {
        return type.name.contains("CHESTPLATE")
    }

    fun ItemStack.isLeggings(): Boolean {
        return type.name.contains("LEGGINGS")
    }

    fun ItemStack.isBoots(): Boolean {
        return type.name.contains("BOOTS")
    }
    fun clear(){
        items.clear()
        itemsPerChest.clear()
        for (group in groups){
            group.clear()
        }
        groups.clear()
    }

    fun add(item: ItemRandom) {
        items.add(item)
    }


    fun fill(vararg inventories: Inventory): List<ItemStack> {
        val chestAmount = inventories.size
        val sortedItemsList = randomize()
        val perInventoryItems = (sortedItemsList.size.toDouble() / chestAmount.toDouble()).toInt()+1
        //Mine.console("§cSortedItems: §e"+ sortedItemsList.size.format())
        //Mine.console("§cChestAmount: §e"+ chestAmount.format())
        //Mine.console("§cPerInventoryItems: §e"+ perInventoryItems.format())
        val listUsed = sortedItemsList.toMutableList()

        for (chestInv in inventories) {
            //Mine.console("§cNew Inventory/Chest")
            if (!canAcumulate) {
                chestInv.clear()
            }

            for (chestItem in itemsPerChest) {
                chestInv.addItem(chestItem.create())
                chestItem.repeats = 0
            }
            var sorted = 0
            while (sorted < perInventoryItems && listUsed.isNotEmpty()) {
                val randomItemSelected = listUsed.random()
                listUsed.remove(randomItemSelected)
                chestInv.addItem(randomItemSelected)
                sorted++
            }

            if (needShuffle) {
                chestInv.contents = chestInv.contents.shuffle()
            }
        }
        return sortedItemsList
    }

}
