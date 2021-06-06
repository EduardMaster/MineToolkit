package net.eduard.api.server.minigame


import net.eduard.api.lib.game.ItemRandom
import net.eduard.api.lib.kotlin.randomByPercent
import net.eduard.api.lib.kotlin.shuffle
import net.eduard.api.lib.modules.ItemCategory
import net.eduard.api.lib.modules.Mine

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * Representa um tipo de Bau do minigame
 */
class MinigameChest {
    var refilTime = 2 * 60
    var maxItems = 6
    var canAcumulate = false
    var needShuffle = true
    var canRepeat = false
    var armorsAmount = 4
    var items = mutableListOf<ItemRandom>()
    var swords = mutableListOf<ItemRandom>()
    var armors = mutableListOf<ItemRandom>()
    var bows = mutableListOf<ItemRandom>()

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

    fun fill(inv: Inventory) : List<ItemStack> {
        val sortedItemsList = mutableListOf<ItemStack>()
        var sortedItems = 0
        val list = items.toMutableList()
        if (!canAcumulate) {
            inv.clear()
        }
        if (swords.isNotEmpty()) {
            val sword = swords.randomByPercent { chance }
            val item = sword.createRandom()
            inv.setItem(inv.firstEmpty(), item)
            sortedItems++
            sortedItemsList.add(item)
        }
        if (bows.isNotEmpty()) {
            val bow = bows.randomByPercent { chance }
            val item = bow.createRandom()
            inv.setItem(inv.firstEmpty(), item)
            sortedItems++
            sortedItemsList.add(item)
        }
        if (armors.isNotEmpty()) {
            val armorList = armors.toMutableList()
            for (armorSorted in 0 until armorsAmount) {
                val armor = armorList.randomByPercent { chance }
                val item = armor.createRandom()
                if (!canRepeat) {
                    for (armorLoop in armors) {
                        if (item.isBoots() && armorLoop.item!!.isBoots()) {
                            armorList.remove(armorLoop)
                        }
                        if (item.isChestplate() && armorLoop.item!!.isChestplate()) {
                            armorList.remove(armorLoop)
                        }
                        if (item.isLeggings() && armorLoop.item!!.isLeggings()) {
                            armorList.remove(armorLoop)
                        }
                        if (item.isHelmet() && armorLoop.item!!.isHelmet()) {
                            armorList.remove(armorLoop)
                        }
                    }
                }
                sortedItems++
                inv.setItem(inv.firstEmpty(), item)
                sortedItemsList.add(item)
                if (armorList.isEmpty()) {
                    break
                }
            }
        }

        if (items.isEmpty()){
            return sortedItemsList
        }

        while (sortedItems < maxItems) {
            val itemRandom = list.randomByPercent { chance }
            val item = itemRandom.createRandom()
            if (!canRepeat){
                list.remove(itemRandom)
            }
            inv.setItem(inv.firstEmpty(), item)
            sortedItemsList.add(item)
            sortedItems++
            if (list.isEmpty()){
                break
            }
        }

        if (needShuffle) {
          inv.contents= inv.contents.shuffle()
        }
        return sortedItemsList
    }

}
