package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetPlayerEvent
import net.eduard.api.lib.manager.EventsManager
import lib.modules.Mine
import lib.modules.MineReflect
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority

/**
 * Listener feito apenas para o Evento PlayerTargetEvent que ocorre a cada 1s
 *
 * @since 2.8
 *
 * @author Eduard
 */
class PlayerTargetListener : EventsManager() {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onTarget(e: PlayerTargetPlayerEvent) {
        val player = e.target
        lib.modules.MineReflect.sendActionBar(e.player,
            lib.modules.Mine.getReplacers(
                EduardAPI.instance.
                message("player target"), player))
 }

}
