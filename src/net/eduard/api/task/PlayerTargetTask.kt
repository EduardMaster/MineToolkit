package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetEvent
import net.eduard.api.lib.modules.Mine
import org.bukkit.scheduler.BukkitRunnable

class PlayerTargetTask : BukkitRunnable() {
    override fun run() {
        for (p in Mine.getPlayers()) {
            try {
                val event = PlayerTargetEvent(p,
                        Mine.getTarget(p, Mine.getPlayerAtRange(p.location, 100.0)))
                Mine.callEvent(event)
            } catch (ex: Exception) {
                EduardAPI.Companion.instance.log("Erro ao causar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }

        }
    }
}
