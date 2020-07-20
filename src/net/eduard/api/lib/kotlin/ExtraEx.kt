package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.java.JavaPlugin


inline fun Number.format() = Extra.formatMoney(this.toDouble())

fun Int.centralized(): Int {
    while (Extra.isColumn(this, 1) || Extra.isColumn(this, 9)) {
        this.inc()
    }
    return this
}

inline fun String.formatColors(): String {
    return Extra.formatColors(this)
}


inline fun Integer.chance(): Boolean {
    return (this.toDouble() / 100).chance()
}

inline fun Double.chance(): Boolean {
    return Extra.getChance(this)
}

inline fun String.cut(maxSize: Int): String {
    return Extra.cutText(this, maxSize)
}

inline fun String.lowerContains(msg: String) = Extra.contains(this, msg)








