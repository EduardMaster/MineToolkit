package net.eduard.api.lib.event

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import org.bukkit.material.MaterialData

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
    var dropsMultiplied = false
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

    fun setDrop(item: ItemStack, amount: Double) {
        drops[item] = amount
    }

    fun addDrop(item: ItemStack, amount: Double) {
        drops[item] = amount + (drops[item] ?: 0.0)
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
            val blockDrop = entry.key
            if (blockDrop.data !in fortuneBlocks) continue
            val amountBase = entry.value
            val amountMultiplied = amountBase * multiplier
            entry.setValue((amountMultiplied * fortuneLevel))
        }
        fixDropsAmount()
        dropsMultiplied = true
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