package net.eduard.api.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.minigame.MinigameSchematic
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Listener para meu Sistema de WorldEdit customizado
 *
 * @author Eduard
 */
class EduWorldEditListener : EventsManager() {


    @EventHandler
    fun onSelectPositions(e: PlayerInteractEvent) {
        val player = e.player
        if (player.gameMode != GameMode.CREATIVE) return
        if (e.item == null)
            return
        if (e.item.type != Material.IRON_AXE) return
        val mapa = MinigameSchematic.getSchematic(player)
        if (e.action == Action.LEFT_CLICK_BLOCK) {
            e.isCancelled = true
            val vec = e.clickedBlock.location.toVector()
            mapa.low = vec
            player.sendMessage("§aPosição 1 setada! §2x: ${vec.x.toInt()} y: ${vec.y.toInt()} z: ${vec.z.toInt()}")
        } else if (e.action == Action.RIGHT_CLICK_BLOCK) {
            val vec = e.clickedBlock.location.toVector()
            mapa.high = vec
            player.sendMessage("§aPosição 2 setada! §2x: ${vec.x.toInt()} y: ${vec.y.toInt()} z: ${vec.z.toInt()}")
        }
    }

}
