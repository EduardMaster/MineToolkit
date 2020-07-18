package net.eduard.api.lib.game

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
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
        if (Mine.getChance(chance)) {
            val clone = item!!.clone()
            val amount = Extra.getRandomInt(minAmount, maxAmount)
            clone.amount = amount
            return clone
        }
        return ItemStack(Material.AIR)
    }

}