package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.event.PlayerTargetPlayerEvent
import net.eduard.api.lib.kotlin.call
import net.eduard.api.lib.modules.Mine
import org.bukkit.scheduler.BukkitRunnable

class PlayerTargetPlayerTask : BukkitRunnable() {
    override fun run() {
        for (player in Mine.getPlayers()) {
            try {
                val target = Mine.getTarget(player, Mine.getPlayerAtRange(player.location, 100.0)) ?: continue
                if (target.hasMetadata("NPC"))continue
                PlayerTargetPlayerEvent(
                    target, player).call()
            } catch (ex: Exception) {
                EduardAPI.instance.log("Erro ao causar o Evento PlayerTargetEvent ")
                ex.printStackTrace()
            }

        }
    }
}
