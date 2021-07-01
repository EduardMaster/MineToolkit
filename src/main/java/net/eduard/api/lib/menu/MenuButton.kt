package net.eduard.api.lib.menu

import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

open class MenuButton(
    var name: String = "Botao",
    @Transient
    var parentMenu: Menu? = null,
    positionX: Int = 0,
    positionY: Int = 1,
    var page: Int = 1

) : Slot(positionX, positionY) {


    var menu: Menu? = null

    var fixed = false

    var autoUpdate = true
    var autoUpdateDelayTicks = 20
    @Transient
    var autoUpdateLasttime = 0L
    @Transient
    var updateHandler : (MenuButton.(Inventory,Player) -> Unit)? = null
    @Transient
    var iconPerPlayer: (Player.() -> ItemStack)? = null
    @Transient
    var hideWhen : (Player.() -> Boolean)? = null
    fun canAutoUpdate() = System.currentTimeMillis() >= autoUpdateLasttime + autoUpdateDelayTicks * 50

    init {
        parentMenu?.addButton(this)
    }

    fun subMenu(title: String, lineAmount: Int, setup: Menu.() -> Unit) {
        this.menu = Menu(title, lineAmount)
        setup(this.menu!!)
    }

    fun subShop(title: String, lineAmount: Int, setup: Shop.() -> Unit) {
        this.menu = Shop(title, lineAmount)
        setup(this.shop)
    }


    val shop: Shop
        get() = menu as Shop

    @Transient
    var click: ClickEffect? = null

    open fun updateButton(inventory: Inventory, player: Player) {
        updateHandler?.invoke(this,inventory,player)
        if (hideWhen!= null){
            if (hideWhen!!(player))return
        }
        var icon = getIcon(player)
        if (parentMenu!!.isTranslateIcon) {
            icon = Mine.getReplacers(icon, player)
        }
        val data = MineReflect.getData(icon)
        data.setString("button-name", name)
        icon = MineReflect.setData(icon, data)
        inventory.setItem(index, icon)


    }



    open fun getIcon(player: Player): ItemStack {
        return iconPerPlayer?.invoke(player) ?: icon
    }

    var icon: ItemStack
        get() = item ?: ItemBuilder(Material.BARRIER)
            .name("§cItem nulo")
        set(itemParameter) {
            item = itemParameter
        }


    val isCategory: Boolean
        get() = menu != null

}