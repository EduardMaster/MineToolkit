package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.EventsManager

import net.eduard.api.core.PlayerSkin
import net.eduard.api.lib.event.BlockMineEvent
import net.eduard.api.lib.kotlin.mineCallEvent
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.util.concurrent.CompletableFuture

/**
 * Pequenas manipulações de Eventos criados que qualquer servidor precise
 *
 * @since 2.3
 * @version 1.0
 *
 * @author Eduard
 */
class EduardAPIListener : EventsManager() {

    var minerationEventEnabled = EduardAPI.instance.getBoolean("features.block-mine-event")

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBreakCallMineEvent(event: BlockBreakEvent) {
        if (event.block.type == Material.ICE) {
            return
        }
        if (!minerationEventEnabled) return
        val block = event.block
        event.isCancelled = true
        event.expToDrop = 0
        val mineEvent = BlockMineEvent(mutableMapOf(), block, event.player, true, event.expToDrop)
        EduardAPI.instance.asyncTask {
            mineEvent.mineCallEvent()
            if (mineEvent.isCancelled) return@asyncTask
            mineEvent.defaultEventActions()
        }


    }

    @EventHandler
    fun onJoinShowPlugins(e: PlayerJoinEvent) {
        val player = e.player
        if (EduardAPI.instance.getBoolean("features.skins")) {
            PlayerSkin.change(player, player.name)
        }
        if (!EduardAPI.instance.getBoolean("features.show-plugins")) return
        if (!player.hasPermission("eduardapi.plugins")) return
        for (plugin in Bukkit.getPluginManager().plugins) {
            if (plugin !is EduardPlugin) continue
            if (plugin.isEnabled()) {
                player.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().version + "§a esta ativado.")
            } else {
                player.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().version + "§c esta desativado.")
            }
        }
        player.sendMessage("§aCaso deseje comprar mais plugins entre em contato ou no site §bwww.eduard.com.br")

    }

    @EventHandler
    fun onDeathAutoRespawn(e: PlayerDeathEvent) {
        val player = e.entity
        if (EduardAPI.instance.getBoolean("features.auto-respawn")) {
            EduardAPI.instance.syncDelay(1) {
                if (player.isDead) {
                    player.fireTicks = 0
                    try {
                        player.spigot().respawn()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
            }
        }
    }

}
