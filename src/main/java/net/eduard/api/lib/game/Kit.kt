package net.eduard.api.lib.game

import net.eduard.api.lib.modules.Mine
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

class Kit() {


    constructor(block  : Kit.() -> Unit) : this(){
        block.invoke(this)
    }

    class KitUpgrade(
            var level: Int = 1,
            var price: Double = 0.0,
            var cooldown: Long = TimeUnit.HOURS.toMillis(1)

    ) {

        var items = mutableListOf<ItemStack>()
        @Transient
        lateinit var kit: Kit

    }

    fun newUpgrade(level: Int, block : (KitUpgrade.() -> Unit)? = null) : KitUpgrade {
        val upgrade = KitUpgrade(level)
        upgrade.kit = this
        upgrades.add(upgrade)
        block?.invoke(upgrade)
        return upgrade
    }

    var name: String = "kit"
    var menuPosition = 10
    var cooldown: Long = TimeUnit.HOURS.toMillis(1)
    var price = 1000.0
    var isClearInventory = false
    var isFillSoup = false
    var isAutoEquip = true


    var icon : ItemStack = ItemBuilder(Material.DIAMOND_SWORD).name("§aKit")
    var items = mutableListOf<ItemStack>()
    var upgrades = mutableListOf<KitUpgrade>()



    fun give(p: Player, level: Int = 1) {
        val inv = p.inventory
        if (isClearInventory) {
            Mine.clearInventory(p)
        }
        if (level == 1)
            for (item in items) {
                val type = item.type.name
                if (isAutoEquip) {
                    if (type.contains("LEGGINGS")) {
                        inv.leggings = item
                    } else if (type.contains("CHESTPLATE")) {
                        inv.chestplate = item
                    } else if (type.contains("BOOTS")) {
                        inv.boots = item
                    } else if (type.contains("HELMET")) {
                        inv.helmet = item
                    } else {
                        inv.addItem(item)
                    }
                } else {
                    inv.addItem(item)
                }
            }else {

            val upgrade = getUpgrade(level)
            for (item in upgrade.items) {
                val type = item.type.name
                if (isAutoEquip) {
                    if (type.contains("LEGGINGS")) {
                        inv.leggings = item
                    } else if (type.contains("CHESTPLATE")) {
                        inv.chestplate = item
                    } else if (type.contains("BOOTS")) {
                        inv.boots = item
                    } else if (type.contains("HELMET")) {
                        inv.helmet = item
                    } else {
                        inv.addItem(item)
                    }
                } else {
                    inv.addItem(item)
                }
            }
        }
        if (isFillSoup)Mine.fill(inv, ItemStack(Material.MUSHROOM_SOUP))
    }

    fun getUpgrade(level: Int) = upgrades.first { it.level == level }

}