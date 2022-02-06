package net.eduard.api.lib.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

class BlockMineEvent(
    val drops: MutableMap<ItemStack, Double>,
    val block: Block,
   player: Player,
    var useEnchants: Boolean,
    var expToDrop: Int = 1
) : PlayerEvent(player), Cancellable {

    enum class DropDestination {
        GROUND, INVENTORY, STORAGE
    }

    var dropsDestination = mutableMapOf<ItemStack, DropDestination>()
    var dropsStorator: ((ItemStack, Double) -> Unit)? = null
    var needGiveExp = true
    var needBreakBlock = true
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

    fun storeDrops() {
        for ((item, doubleAmount) in drops) {
            if (getDestination(item) == DropDestination.STORAGE) {
                dropsStorator?.invoke(item, doubleAmount)
            }
        }

    }

    fun defaultEventActions() {
        if (needApplyFortune) {
            applyFortune()
        }
        giveDrops()
        storeDrops();
        fallDropsInWorld()
        if (needGiveExp && expToDrop > 0)
            player.giveExp(expToDrop)

        if (needBreakBlock)
            breakBlock()
    }

    fun breakBlock() {
        /*
        val blockInfo = Blocks.get(block.location)
        if (blockInfo != null)
            blockInfo.setType(Material.AIR)
        */

        block.type = Material.AIR
    }

    fun setDrop(item: ItemStack, amount: Double) {
        item.amount = 1
        drops[item] = amount
    }

    fun addDrop(item: ItemStack, amount: Double) {
        item.amount = 1
        if (item in drops){
            val lastDropAmount = drops[item]!!
            drops[item] = amount +  lastDropAmount
        }else{
            drops[item] = amount
        }

    }

    fun hasDrop(item: ItemStack): Boolean {
        val itemAmount = item.amount
        item.amount = 1
        val have = item in drops
        item.amount = itemAmount
        return have
    }

    fun useDefaultDrops() {
        for (dropItem in block.getDrops(player.itemInHand)) {
            val dropAmount = dropItem.amount
            dropItem.amount = 1
            drops[dropItem] = dropAmount.toDouble()
        }
    }

    fun clearDrops() {
        drops.clear()
    }


    fun getDestination(item: ItemStack): DropDestination {
        return dropsDestination[item] ?: DropDestination.GROUND
    }

    fun setDestination(item: ItemStack, destination: DropDestination) {
        dropsDestination[item] = destination
    }

    fun giveDrops() {
        for ((item, doubleAmount) in drops) {
            if (getDestination(item) == DropDestination.INVENTORY) {
                val itemToGive = item.clone()
                itemToGive.amount = if (doubleAmount < Int.MAX_VALUE) doubleAmount.toInt() else Int.MAX_VALUE
                player.inventory.addItem(itemToGive)
            }
        }
    }

    fun fallDropsInWorld() {
        for ((item, doubleAmount) in drops) {
            if (getDestination(item) == DropDestination.GROUND) {
                val itemToGive = item.clone()
                itemToGive.amount = if (doubleAmount < Int.MAX_VALUE) doubleAmount.toInt() else Int.MAX_VALUE
                player.world.dropItemNaturally(block.location, itemToGive)
            }
        }
    }

    fun applyFortune(): Boolean {
        val item = player.itemInHand ?: return false
        val fortuneLevel = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
        if (fortuneLevel <= 0) return false
        for (entry in drops.entries) {
            val blockDrop = entry.key
            if (blockDrop.data !in fortuneBlocks) continue
            val amountBase = entry.value
            val amountMultiplied = amountBase * multiplier
            val result = (amountMultiplied * fortuneLevel)
            entry.setValue(result)
        }
        fortuneApplied = true
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

        var fortuneBlocks = mutableListOf<MaterialData>()

        init {
            for (type in Material.values()) {
                if (type.name.contains("ORE")) {
                    fortuneBlocks.add(MaterialData(type))
                }
            }
            fortuneBlocks.add(MaterialData(Material.NETHER_STALK))
            fortuneBlocks.add(MaterialData(Material.CACTUS))
            fortuneBlocks.add(MaterialData(Material.SEEDS))
            fortuneBlocks.add(MaterialData(Material.MELON_SEEDS))
            fortuneBlocks.add(MaterialData(Material.PUMPKIN_SEEDS))
            fortuneBlocks.add(MaterialData(Material.IRON_BLOCK))
            fortuneBlocks.add(MaterialData(Material.MELON))
            fortuneBlocks.add(MaterialData(Material.LAPIS_BLOCK))
            fortuneBlocks.add(MaterialData(Material.DIAMOND_BLOCK))
            fortuneBlocks.add(MaterialData(Material.LAPIS_ORE))
            fortuneBlocks.add(MaterialData(Material.POTATO_ITEM))
            fortuneBlocks.add(MaterialData(Material.CARROT_ITEM))
            fortuneBlocks.add(MaterialData(Material.DIAMOND))
            fortuneBlocks.add(MaterialData(Material.GOLD_INGOT))
            fortuneBlocks.add(MaterialData(Material.EMERALD))
            // LAPIZ
            fortuneBlocks.add(MaterialData(Material.INK_SACK, 4))
            // COCOA BEANS
            fortuneBlocks.add(MaterialData(Material.INK_SACK, 3))
        }


        @JvmStatic
        val handlerList = HandlerList()

    }

}