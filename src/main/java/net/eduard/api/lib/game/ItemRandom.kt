package net.eduard.api.lib.game

import lib.modules.Extra
import lib.modules.Mine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class ItemRandom(
        var item: ItemStack? = null,
        var chance: Double = 1.0,
        var minAmount: Int = 1,
        var maxAmount: Int = 1
)

{
    fun create(): ItemStack {
        if (item == null ) return ItemStack(Material.AIR)
        if (lib.modules.Mine.getChance(chance)) {
            val clone = item!!.clone()
            val amount = lib.modules.Extra.getRandomInt(minAmount, maxAmount)
            clone.amount = amount
            return clone
        }
        return ItemStack(Material.AIR)
    }

}