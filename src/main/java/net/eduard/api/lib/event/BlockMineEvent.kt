package net.eduard.api.lib.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack

class BlockMineEvent(
    val drops: MutableMap<ItemStack, Double>,
    var block: Block,
    var player: Player,
    var useEnchants: Boolean,
    var expToDrop: Int = 1
) : Event(), Cancellable {
    var needGiveDrops = true
    var needFallDropsInWorld = false
    var needGiveExp = true
    var needApplyFortune = true
    var multiplier = 1.0
    var fortuneApplied = false
    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(toggle: Boolean) {
        cancelled = toggle
    }

    fun breakBlock() {
        /*
        val blockInfo = Blocks.get(block.location)
        if (blockInfo != null)
            blockInfo.setType(Material.AIR)
        */

        block.type = Material.AIR
    }

    fun useDefaultDrops() {
        for (dropItem in block.getDrops(player.itemInHand)) {
            drops[dropItem] = dropItem.amount.toDouble()
        }
    }

    fun clearDrops() {
        drops.clear()
    }

    fun fixDropsAmount() {
        for (entry in drops) {
            val item = entry.key
            var amount = entry.value
            if (amount <= 0.0) {
                amount = 1.0
                entry.setValue(amount)
            }
            if (amount < Int.MAX_VALUE) {
                item.amount = amount.toInt()
            } else {
                item.amount = Int.MAX_VALUE
            }
            if (item.amount <= 0) {
                item.amount = 1
            }
        }
    }

    fun giveDrops() {
        fixDropsAmount()
        for (item in drops.keys) {
            player.inventory.addItem(item)
        }
    }

    fun fallDropsInWorld() {
        for (item in drops.keys) {
            player.world.dropItemNaturally(block.location, item)
        }
    }

    fun applyFortune(): Boolean {
        val item = player.itemInHand ?: return false
        val fortuneLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
        if (fortuneLevel <= 0) return false
        for (entry in drops.entries) {
            val amountBase = entry.value
            val amountMultiplied = amountBase * multiplier
            entry.setValue((amountMultiplied * fortuneLevel))
        }
        fixDropsAmount()
        fortuneApplied=true
        return true
    }

    init {
        if (drops.isEmpty()) {
            useDefaultDrops()
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

}