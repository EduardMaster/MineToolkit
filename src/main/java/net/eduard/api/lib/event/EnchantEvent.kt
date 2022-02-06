package net.eduard.api.lib.event

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class EnchantEvent(
    var item : ItemStack ,
    var enchantment : Enchantment ,
    var level : Int, player: Player)

    : PlayerEvent(player) , Cancellable {

    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }
    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

}