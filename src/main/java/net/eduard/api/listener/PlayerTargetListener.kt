package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.lib.abstraction.Minecraft
import net.eduard.api.lib.event.PlayerTargetPlayerEvent
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerMoveEvent

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
        Minecraft.getInstance().sendActionBar(
            e.player,
            Mine.getReplacers(
                EduardAPI.instance.getString("player target"), e.target
            )
        )
    }

}
