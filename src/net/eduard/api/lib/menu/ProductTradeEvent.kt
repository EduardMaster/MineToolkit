package net.eduard.api.lib.menu

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class ProductTradeEvent(player: Player) : PlayerEvent(player), Cancellable {
    lateinit var product: Product
    lateinit var shop: Shop
    var amount = 0.0
    var newStock = 0.0
    var type: TradeType = TradeType.SELABLE
    private var cancelled: Boolean = false
    var balance = 0.0
    var priceTotal = 0.0

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
            @JvmStatic
            get
    }
}
