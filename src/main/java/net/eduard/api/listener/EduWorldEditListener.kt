package net.eduard.api.listener

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.server.minigame.GameSchematic
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
        if (player.gameMode == GameMode.CREATIVE) {
            if (e.item == null)
                return
            if (e.item.type == Material.WOOD_AXE) {
                val mapa = GameSchematic.getSchematic(player)
                if (e.action == Action.LEFT_CLICK_BLOCK) {
                    mapa.high = e.clickedBlock.location.toVector()
                    player.sendMessage("§aPosição 1 setada!")
                } else if (e.action == Action.RIGHT_CLICK_BLOCK) {
                    mapa.low = e.clickedBlock.location.toVector()
                    player.sendMessage("§aPosição 2 setada!")
                }
            }
        }
    }

}
