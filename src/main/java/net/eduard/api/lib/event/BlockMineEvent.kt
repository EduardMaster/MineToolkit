package net.eduard.api.lib.event

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack

class BlockMineEvent(
    val drops: MutableMap<ItemStack, Double>,
    block: Block,
    player: Player
) : BlockBreakEvent(block, player) {

    fun useDefaultDrops() {
        for (dropItem in block.getDrops(player.itemInHand)) {
            drops[dropItem] = dropItem.amount.toDouble()
        }

    }

    init {
        if (drops.isEmpty()) {
            useDefaultDrops()
        }
    }

}