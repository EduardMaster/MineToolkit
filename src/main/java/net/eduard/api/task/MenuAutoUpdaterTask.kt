package net.eduard.api.task

import net.eduard.api.EduardAPI
import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.menu.getMenu
import net.eduard.api.lib.modules.Mine
import java.lang.Exception

class MenuAutoUpdaterTask : TimeManager(EduardAPI.instance.configs
    .getLong("menu-updater.ticks")) {

    override fun run() {
        for (player in Mine.getPlayers()) {
            val menu = player.getMenu() ?: continue
            try {
                val pagina = menu.getPageOpen(player)
                val inventory = player.openInventory.topInventory
                menu.update(inventory,player,pagina , false)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}