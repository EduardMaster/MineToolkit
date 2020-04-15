package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetEvent
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler

/**
 * Listener feito apenas para o Evento PlayerTargetEvent que ocorre a cada 1s
 *
 * @since 2.8
 *
 * @author Eduard
 */
class PlayerTargetAtEntityListener : EventsManager() {


    @EventHandler
    fun onTarget(e: PlayerTargetEvent) {

        if (e.entity is Player) {
            val player = e.entity as Player
            MineReflect.sendActionBar(e.player, Mine.getReplacers(EduardAPI.instance.message("player information"), player))
        }
    }

}
