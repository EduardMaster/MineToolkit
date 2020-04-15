package net.eduard.api.lib.menu

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class ProductTradeEvent(player: Player) : PlayerEvent(player), Cancellable {
    var product: Product? = null
    var shop: Shop? = null
    var amount: Double = 0.toDouble()
    var newStock: Double = 0.toDouble()
    var type: TradeType? = null
    private var cancelled: Boolean = false
    var balance: Double = 0.toDouble()
    var priceTotal: Double = 0.toDouble()

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel

    }

    companion object {

        val handlerList = HandlerList()
    }

}
