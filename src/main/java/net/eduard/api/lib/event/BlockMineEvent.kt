package net.eduard.api.lib.event

import net.eduard.api.lib.abstraction.Minecraft
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

class BlockMineEvent(
    val drops: MutableMap<ItemStack, Double>,
    val block: Block,
    val player: Player,
    var useEnchants: Boolean,
    var expToDrop: Int = 1
) {

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
    var isCancelled = false


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

        if (needGiveExp && expToDrop > 0)
            player.giveExp(expToDrop)
        if (needBreakBlock)
            breakBlock()

        fallDropsInWorld()
    }

    fun breakBlock() {
        //block.type = Material.AIR
        val chunk = block.chunk
        Minecraft.instance.setBlock(block , chunk, Material.AIR, 0, false)
    }

    fun setDrop(item: ItemStack, amount: Double) {
        item.amount = 1
        drops[item] = amount
    }

    fun addDrop(item: ItemStack, amount: Double) {
        item.amount = 1
        if (item in drops) {
            val lastDropAmount = drops[item]!!
            drops[item] = amount + lastDropAmount
        } else {
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

    abstract class BlockMineListener(val priority: Int) {
        fun register() {
            listeners[priority] = this
        }

        fun unregister() {
            listeners.remove(priority)
        }

        abstract fun modify(event: BlockMineEvent)

    }

    companion object {

        val listeners = mutableMapOf<Int, BlockMineListener>()

        fun registerListener(listener: BlockMineListener) {
            listeners[listener.priority] = listener
        }

        fun unregisterListener(listener: BlockMineListener) {
            listeners.remove(listener.priority, listener)

        }
        fun callEvent(event: BlockMineEvent) {
            callEvent(null, event)

        }
        fun callEvent(originalEvent: BlockBreakEvent?, event: BlockMineEvent) {
            var priority = 0;
            while (priority < 10) {
                try {
                    listeners[priority]?.modify(event)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                if (event.isCancelled) {
                    return
                }
                priority++
            }
            event.defaultEventActions()
            if (!event.isCancelled && event.needBreakBlock) {
                originalEvent?.isCancelled = false
            }

        }

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


    }

}