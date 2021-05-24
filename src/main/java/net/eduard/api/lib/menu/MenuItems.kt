package net.eduard.api.lib.menu

import net.eduard.api.lib.kotlin.player
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MenuItems<T : Any> : MenuButton("Items") {
    @Transient
    var items : (Player.() -> Map<String,T>)? = null
    @Transient
    var display : (Player.(T) -> ItemStack)? = null
    @Transient
    var action : (Player.(T) -> Unit)? = null
    init{
        click = ClickEffect { e ->
            if (e.currentItem == null)return@ClickEffect
            val player = e.player
            val map = items?.invoke(player)?:return@ClickEffect
            val data = MineReflect.getData(icon)
            val identity =  data.getString("item-name")
            val item = map[identity]!!
            action?.invoke(player,item)

        }
    }

    override fun update(player: Player, inventory: Inventory) {
        val map = items?.invoke(player)?:return
        var slotUsed = index
        for ((identity, item) in map){
            var icon = display?.invoke(player,item) ?:continue
            val data = MineReflect.getData(icon)
            data.setString("button-name", name)
            data.setString("item-name", identity)
            icon = MineReflect.setData(icon, data)
            inventory.setItem(slotUsed, icon)
            slotUsed++
        }

    }
}