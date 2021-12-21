package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetPlayerEvent
import net.eduard.api.lib.kotlin.mineCallEvent
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.modules.Mine
import org.bukkit.scheduler.BukkitRunnable

class PlayerTargetPlayerTask : TimeManager(20L) {
    override fun run() {
        for (player in Mine.getPlayers()) {
            try {
                val target = Mine.getTarget(player,
                    Mine.getPlayerAtRange(player.location, 100.0)) ?: continue
                if (target.hasMetadata("NPC"))continue
                EduardAPI.instance.syncTask {
                    PlayerTargetPlayerEvent(
                        target, player).mineCallEvent()
                }
            } catch (ex: Exception) {
                EduardAPI.instance.log("Erro ao executar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }
        }
    }
}
