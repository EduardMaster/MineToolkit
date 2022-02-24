package net.eduard.api.lib.menu

import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import net.eduard.api.lib.storage.annotations.StorageReference
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.function.BiConsumer
import java.util.function.Function
import java.util.function.Predicate

open class MenuButton(
    var name: String,
    @Transient
    var parentMenu: Menu?,
    positionX: Int,
    positionY: Int,
    var page: Int
) : Slot(null,positionX, positionY) {
    var menu: Menu? = null
    @StorageReference
    var menuLink: Menu? = null
    var fixed = false
    var autoUpdate = true
    var autoUpdateDelayTicks = 20
    @Transient
    var autoUpdateLasttime = 0L

    @Transient
    var updateHandler: BiConsumer<Inventory, Player>? = null

    var iconPerPlayer: (Player.() -> ItemStack)?
        set(value) {
            iconGenerated = IconGenerated(value!!)
        }
        get() = null

    @Transient
    var iconGenerated: Function<Player, ItemStack>? = null

    @Transient
    var hideWhen: Predicate<Player>? = null

    fun canAutoUpdate() = System.currentTimeMillis() >= autoUpdateLasttime + autoUpdateDelayTicks * 50

    constructor(name: String) : this(name, null, 1, 1, 1)
    constructor(name: String, menu: Menu) : this(name, menu, 1, 1, 1)
    constructor(
        name: String, menu: Menu?, positionX: Int,
        positionY: Int
    ) : this(name, menu, positionX, positionY, 1)

    constructor(
        name: String, positionX: Int,
        positionY: Int
    ) : this(name, null, positionX, positionY, 1)

    constructor() : this("BotaoVazio")
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
        updateHandler?.accept(inventory, player)
        if (hideWhen?.test(player) == true) return
        var icon = getIcon(player)
        if (parentMenu?.isTranslateIcon == true) {
            icon = Mine.applyPlaceholders(icon, player)
        }
        if (icon.type != Material.AIR) {
            val data = MineReflect.getData(icon)
            data.setString("button-name", name)
            icon = MineReflect.setData(icon, data)
        }
        inventory.setItem(index, icon)
    }


    open fun getIcon(player: Player): ItemStack {
        return iconGenerated?.apply(player) ?: icon
    }

    var icon: ItemStack
        get() = item ?: ItemBuilder(Material.BARRIER)
            .name("§cItem nulo")
        set(itemParameter) {
            item = itemParameter
        }


    val isCategory: Boolean
        get() = menu != null || menuLink != null


}