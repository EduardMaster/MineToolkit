package net.eduard.api.lib.event

import lib.game.DisplayBoard
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class ScoreUpdateEvent(var score: lib.game.DisplayBoard, who: Player) : PlayerEvent(who), Cancellable {

    private var cancelled = false
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

}