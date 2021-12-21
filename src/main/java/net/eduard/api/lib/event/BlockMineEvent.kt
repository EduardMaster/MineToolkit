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
    var useEnchants : Boolean,
    var expToDrop: Int = 1
) : Event(), Cancellable {
    var needGiveDrops = true
    var needGiveExp = true
    var needApplyFortune = true
    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }
    override fun setCancelled(toggle: Boolean) {
        cancelled = toggle
    }

    fun breakBlock(){
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

    fun giveDrops() {
        for ((item, amount) in drops) {
            item.amount = amount.toInt()
            player.inventory.addItem(item)
        }
    }

    fun dropInWorld() {
        for (item in drops.keys) {
            player.world.dropItemNaturally(block.location, item)
        }
    }

    fun applyFortune() {
        val item = player.itemInHand?:return
        val level = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)?:0
        for ((loopItem, amount) in drops) {
            drops[loopItem] = amount + (amount * level)
        }
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