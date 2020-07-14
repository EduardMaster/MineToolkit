package net.eduard.api.lib.menu

import net.eduard.api.lib.game.ClickEffect
import net.eduard.api.lib.kotlin.player
import net.eduard.api.lib.modules.Copyable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

open class MenuButton(var name: String = "Botao"
                      ,     @Transient
                      var parentMenu: Menu? = null
                 , positionX: Int = 0,
                 positionY: Int = 1,
                 var page: Int = 1

                 ) : Slot(positionX, positionY)  {

   companion object{
         val NO_ACTION = ClickEffect { }
   }



    var menu: Menu? = null

    init {
        if (parentMenu!= null){
            parentMenu?.addButton(this)
        }
    }

    constructor(parent: Menu) : this(parentMenu =  parent)



    val shop: Shop
        get() = menu as Shop

    @Transient
    var click: ClickEffect = NO_ACTION

    open fun getIcon(player: Player): ItemStack {
        return this.icon
    }

    var icon: ItemStack
        get() = item!!
        set(icon) {
            item = icon
        }

    val isCategory: Boolean
        get() = menu != null

}