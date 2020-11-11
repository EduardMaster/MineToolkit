package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetPlayerEvent
import net.eduard.api.lib.kotlin.call
import net.eduard.api.lib.modules.Mine
import org.bukkit.scheduler.BukkitRunnable

class PlayerTargetPlayerTask : BukkitRunnable() {
    override fun run() {
        for (p in Mine.getPlayers()) {
            try {
                val target =
                        Mine.getTarget(p, Mine.getPlayerAtRange(p.location, 100.0)) ?: continue

                PlayerTargetPlayerEvent(
                    target, p).call()
            } catch (ex: Exception) {
                EduardAPI.instance.log("Erro ao causar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }

        }
    }
}
