package net.eduard.api.lib.menu

import net.eduard.api.lib.kotlin.player
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class MenuItems<T : Any> : MenuButton("Items") {
    @Transient
    var items: (Player.() -> Map<String, T>)? = null

    @Transient
    var display: (Player.(T) -> ItemStack)? = null

    @Transient
    var action: (Player.(T) -> Unit)? = null

    init {
        click = ClickEffect { e ->
            if (e.currentItem == null) return@ClickEffect
            val player = e.player
            val map = items?.invoke(player) ?: return@ClickEffect
            val data = MineReflect.getData(e.currentItem)
            val identity = data.getString("item-name")
            val item: T = map[identity] ?: return@ClickEffect
            action?.invoke(player, item)

        }
    }

    fun nextPosition(slot: Int): Int {
        val menu = parentMenu!!
        var slotUsed = slot
        slotUsed++
        if (slotUsed < 0) {
            slotUsed = 0
        }
        for (line in menu!!.autoAlignSkipLines) {
            val currentLine = Extra.getLine(slotUsed)
            val currentCollumn = Extra.getColumn(slotUsed)
            val restColumn = 9 - currentCollumn
            if (line == currentLine) {
                slotUsed += (restColumn)
            }
        }
        for (column in menu!!.autoAlignSkipColumns) {
            if (Mine.isColumn(slotUsed, column))
                slotUsed++
        }
        if (slotUsed >= menu!!.slotLimit) {
            slotUsed = 0
            //terminar aqui avançar pagina
            /*
           lastPage++
           pageAmount++
           lastSlot = 0

             */
        }
        return slotUsed
    }

    override fun update(player: Player, inventory: Inventory) {
        val map = items?.invoke(player) ?: return
        var slotUsed = 0
        for ((identity, item) in map) {
            slotUsed = nextPosition(slotUsed)
            var icon = display?.invoke(player, item) ?: continue
            val data = MineReflect.getData(icon)
            data.setString("button-name", name)
            data.setString("item-name", identity)
            icon = MineReflect.setData(icon, data)
            inventory.setItem(slotUsed, icon)

        }


    }
}