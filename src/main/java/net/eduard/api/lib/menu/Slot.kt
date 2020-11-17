package net.eduard.api.lib.menu

import net.eduard.api.lib.manager.EffectManager
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.io.Serializable


open class Slot(
        var positionX : Int = 0,
        var positionY : Int = 0,
        var item: ItemStack? = null

) : Serializable {

    var effects: EffectManager? = null

    constructor(item: ItemStack?, index: Int) : this(item = item) {
        this.index = index
    }
    val slot: Int
        get() = index

    var index: Int
        get() = Extra.getIndex(positionX, positionY)
        set(index) {
            positionX = Extra.getColumn(index)
            positionY = Extra.getLine(index)
        }


    constructor(item: ItemStack?, positionX: Int, positionY: Int) :
            this(positionX,positionY,item)


    override fun equals(other: Any?): Boolean {
        if (other == null)return false
        if (other is Slot){
            return positionX == other.positionX &&
                    positionY == other.positionY
        }
        if (other is ItemStack){
            return this.item == other
        }
        return false
    }


    fun setSlot(slot: Slot) {
        index = slot.index
        item = slot.item
    }

    open fun copy(): Slot {
        return Copyable.copyObject(this)
    }

    fun setPosition(collumn: Int, line: Int) {
        positionX = collumn
        positionY = line
    }

    fun give(inventory: Inventory) {
        inventory.setItem(index, item)
    }

    fun give(menu: Inventory, placeholders: Map<String?, String?>?) {
        val clone = item?.clone()
        Mine.applyPlaceholders(clone, placeholders)
        menu.setItem(index, clone)
    }

    override fun hashCode(): Int {
        var result = positionX
        result = 31 * result + positionY
        return result
    }

}