package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetPlayerEvent
import net.eduard.api.lib.kotlin.call
import lib.modules.Mine
import org.bukkit.scheduler.BukkitRunnable

class PlayerTargetPlayerTask : BukkitRunnable() {
    override fun run() {
        for (p in lib.modules.Mine.getPlayers()) {
            try {
                val target =
                        lib.modules.Mine.getTarget(p, lib.modules.Mine.getPlayerAtRange(p.location, 100.0)) ?: continue
                val event = PlayerTargetPlayerEvent(
                        target, p)
                event.call()
            } catch (ex: Exception) {
                EduardAPI.instance.log("Erro ao causar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }

        }
    }
}
