package net.eduard.api.task

import net.eduard.api.lib.manager.TimeManager
import net.eduard.api.lib.menu.getMenu
import net.eduard.api.lib.modules.Mine
import java.lang.Exception

class MenuAutoUpdaterTask : TimeManager(1L) {

    override fun run() {
        for (player in Mine.getPlayers()){
            val menu = player.getMenu()?:continue
            try {
                val pagina = menu.getPageOpen(player)
                val inventory = player.openInventory.topInventory
                var itemsChanged = 0
                for (button in menu.buttons){
                    if (button.page != pagina)continue
                    if (!button.autoUpdate)continue
                    if (!button.canAutoUpdate())continue
                    button.autoUpdateLasttime = System.currentTimeMillis()
                    button.updateButton(inventory, player)
                    itemsChanged++
                }
                if (itemsChanged>0){
                    player.updateInventory()
                }
            }catch (ex : Exception){
                ex.printStackTrace()
            }
        }
    }
}