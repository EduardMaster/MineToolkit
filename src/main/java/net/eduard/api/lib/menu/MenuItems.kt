package net.eduard.api.lib.menu

import net.eduard.api.lib.kotlin.player
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MenuItems<T : Any> {
    @Transient
    var items: (Player.() -> Map<String, T>)? = null

    @Transient
    var display: (Player.(T) -> ItemStack)? = null

    @Transient
    var action: (Player.(T) -> Unit)? = null

    lateinit var menu : Menu

    val click  = ClickEffect { e ->
        if (e.currentItem == null) return@ClickEffect
        val player = e.player
        val map = items?.invoke(player) ?: return@ClickEffect
        val data = MineReflect.getData(e.currentItem)
        val identity = data.getString("item-name")?:return@ClickEffect
        val item: T = map[identity] ?: return@ClickEffect
        action?.invoke(player, item)

    }

    init {

    }


    fun update( inventory: Inventory,player: Player) {
        val map = items?.invoke(player) ?: return
        menu.pageAmount=1
        menu.lastPage=1
        menu.lastSlot=0
        for ((identity, item) in map) {
            menu.nextPosition()

            var icon = display?.invoke(player, item) ?: continue
            val data = MineReflect.getData(icon)
            //data.setString("button-name", name)
            data.setString("item-name", identity)
            icon = MineReflect.setData(icon, data)
            inventory.setItem(menu.lastSlot, icon)

        }


    }
}