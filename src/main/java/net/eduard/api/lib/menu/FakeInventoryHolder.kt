package net.eduard.api.lib.menu

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class FakeInventoryHolder(val menu: Menu) : InventoryHolder {
    lateinit var openInventory: Inventory
    var pageOpenned = 1

    override fun getInventory(): Inventory {
        return inventory
    }

}