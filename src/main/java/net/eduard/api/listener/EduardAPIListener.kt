package net.eduard.api.listener

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.EventsManager

import net.eduard.api.core.PlayerSkin
import net.eduard.api.lib.event.BlockMineEvent
import net.eduard.api.lib.kotlin.mineCallEvent
import net.eduard.api.lib.modules.Mine
import net.eduard.api.server.EduardPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerListPingEvent

/**
 * Pequenas manipulações de Eventos criados que qualquer servidor precise
 *
 * @since 2.3
 * @version 1.0
 *
 * @author Eduard
 */
class EduardAPIListener : EventsManager() {


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    fun onBreakCallMineEvent(e: BlockBreakEvent) {
        if (!EduardAPI.instance.getBoolean("features.block-mine-event"))return
        val event = BlockMineEvent(mutableMapOf(), e.block, e.player, true, e.expToDrop)
        e.isCancelled = true
        event.mineCallEvent()
        if (event.isCancelled) return
        if (event.needApplyFortune)
            event.applyFortune()
        if (event.needGiveDrops)
            event.giveDrops()
        else event.dropInWorld()
        if (event.needGiveExp && event.expToDrop > 0)
            event.player.giveExp(e.expToDrop)
        event.breakBlock()
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
                player.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv"
                + plugin.getDescription().version + "§a esta ativado.")
            } else {
                player.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv"
                + plugin.getDescription().version + "§c esta desativado.")
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
