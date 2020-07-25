package net.eduard.api.lib.menu

import java.util.ArrayList
import java.util.HashMap

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.game.ClickEffect
import net.eduard.api.lib.kotlin.centralized
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.plugin.IPluginInstance
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception
import kotlin.math.log

/**
 * Sistema proprio de criacao de Menus Interativos automaticos para facilitar
 * sua vida
 *
 * @author Eduard
 */
open class Menu(

        var title: String = "Menu",
        var lineAmount: Int = 1,
        block: (Menu.() -> Unit)? = null

) : EventsManager() {

    fun button(name: String = "Botao", block: (MenuButton.() -> Unit)? = null): MenuButton {
        return MenuButton(name, this, block = block)
    }

    @Transient
    var superiorMenu: Menu? = null

    @Transient
    var openHandler: (Menu.(Inventory, Player) -> Unit)? = null


    var pageAmount = 1
    var pagePrefix = ""
    var pageSuffix = "(\$page/\$max_page)"

    var isTranslateIcon: Boolean = false
    var isAutoAlignItems: Boolean = false
    var autoAlignSkipCenter = true
    var isCacheInventories: Boolean = false
    var openWithItem: ItemStack? = Mine.newItem(Material.COMPASS, "§aMenu Exemplo", 1, 0, "§2Clique abrir o menu")
    var openWithCommand: String? = null

    var openNeedPermission: String? = null
    var messagePermission = "§cVocê precisa de permissão do Cargo Master para abrir este menu."
    var previousPage = Slot(
            Mine.newItem(Material.ARROW, "§aVoltar Página", 1, 0, "§2Clique para ir para a página anterior"), 5, 3)
    var backPage = Slot(
            Mine.newItem(Material.ARROW, "§aVoltar para Menu Principal", 1, 0, "§2Clique para ir para a página superior"), 1, 2)
    var nextPage = Slot(
            Mine.newItem(Material.ARROW, "§aPróxima Página", 1, 0, "§2Clique para ir para a próxima página"), 9, 2)
    var buttons = mutableListOf<MenuButton>()


    inline fun cantBeOpened() {
        openWithItem = null
        openWithCommand = null
    }

    @Transient
    var effect: ClickEffect = MenuButton.NO_ACTION

    @Transient
    private var pagesCache: MutableMap<Int, Inventory> = HashMap()

    @Transient
    private val pageOpened = HashMap<Player, Int>()

    init {
        block?.invoke(this)
    }

    val fullTitle: String
        get() = if (isPageSystem) {
            pagePrefix + title + pageSuffix
        } else title

    val firstEmptySlotOnInventories: Int
        get() {
            var page = 1
            var id = -1

            while (id == -1 && page <= pageAmount)
                id = getFirstEmptySlot(++page)

            return id
        }

    val isFull: Boolean
        get() = isFull(1)

    val isPageSystem: Boolean
        get() = pageAmount > 1

    fun removeButton(name: String) {
        val button = getButton(name) ?: return
        buttons.remove(button)

    }

    fun removeAllButtons() {
        buttons.clear()
        clearCache()
        pageOpened.forEach { (p, _) -> p.closeInventory() }
        pageOpened.clear()
    }

    open fun copy(): Menu {

        return Copyable.copyObject(this)
    }

    fun getButton(icon: ItemStack, player: Player): MenuButton? {
        for (button in buttons) {
            if (button.getIcon(player).isSimilar(icon)) {
                return button
            }
        }

        return null
    }

    fun getButton(name: String): MenuButton? {
        for (button in buttons) {
            if (button.name.equals(name, ignoreCase = true)) {
                return button
            }
        }

        return null
    }

    fun getButton(page: Int, index: Int): MenuButton? {
        for (button in buttons) {
            if (button.index == index && page == button.page)
                return button
        }
        return null
    }

    fun addButton(button: MenuButton) {
        buttons.add(button)
    }


    fun removeButton(button: MenuButton) {
        buttons.remove(button)

    }

    fun updateCache(page: Int) {
        if (isCacheInventories) {
            pagesCache.remove(page)
        }
    }



    fun clearCache() {
        pagesCache.clear()
    }

    fun getFirstEmptySlot(page: Int): Int {

      for (slot in 0..(lineAmount * 9)){
          getButton(page, slot) ?: return slot
      }

        return -1
    }

    fun isFull(page: Int): Boolean {

        for (slot in 0..(lineAmount * 9)){
            getButton(page, slot) ?: return false
        }
        return true
    }

    fun open(player: Player): Inventory? {
        return open(player, 1)
    }

    fun open(player: Player, page: Int): Inventory? {

        if (openNeedPermission != null) {
            if (!player.hasPermission(openNeedPermission)) {
                player.sendMessage(messagePermission)


                return null
            }
        }
        var page = page
        if (page < 1) {
            page = 1
        }
        if (page > pageAmount) {
            page = pageAmount
        }

        if (isCacheInventories && pagesCache.containsKey(page)) {
            player.openInventory(pagesCache[page])
        } else {
            if ((lineAmount < 1) or (lineAmount > 6)) {
                lineAmount = 1
            }

            var perPage = lineAmount * 9
            var slot = 0
            if (lineAmount>=2){
                perPage-= 6
                slot = 10
            }
            if (lineAmount>=3){
                perPage-= 6
            }
            perPage -= lineAmount * 2
            if (autoAlignSkipCenter){
                perPage-= lineAmount
            }
            if (isAutoAlignItems){
                pageAmount = 1 + (buttons.size/perPage)
            }
            val prefix = pagePrefix.replace("\$max_page", "" + pageAmount).replace("\$page", "" + page)
            val suffix = pageSuffix.replace("\$max_page", "" + pageAmount).replace("\$page", "" + page)
            var menuTitle = Extra.cutText(prefix + title + suffix, 32)
            if (!isPageSystem &&!isAutoAlignItems) {
                menuTitle = title
            }
            val fakeHolder = FakeInventoryHolder(this)
            val menu = Bukkit.createInventory(fakeHolder, 9 * lineAmount, menuTitle)
            fakeHolder.openInventory = menu
            fakeHolder.pageOpenned = page
            if (isPageSystem) {
                if (page > 1)
                    previousPage.give(menu)
                if (page < pageAmount)
                    nextPage.give(menu)
            }
            if (this.superiorMenu != null) {
                backPage.give(menu)
            }

            if (isAutoAlignItems) {

                var startList = perPage*(page-1)
                var endList = startList + perPage
                if (endList > buttons.size){
                    endList = buttons.size
                }

                var subButtons = buttons.subList(startList,endList)


                for (button in subButtons) {

                    slot = slot.centralized()


                    if (autoAlignSkipCenter){
                        if (Extra.isColumn(slot,5))slot++
                    }

                    var icon = button.getIcon(player)

                    if (isTranslateIcon) {
                        icon = Mine.getReplacers(icon, player)
                    }
                    if (slot >=menu.size){
                        println("Slot $slot" + " acima do limite" + menu.size)
                    }else {
                        menu.setItem(slot, icon)
                    }
                    slot++
                }
            } else {


                for (button in buttons) {
                    if (button.page != page) continue
                    var position = button.index
                    if (position >= lineAmount * 9) {
                        position = 0
                    }
                    var icon = button.getIcon(player)
                    if (isTranslateIcon) {
                        icon = Mine.getReplacers(icon, player)
                    }
                    menu.setItem(position, icon)
                }
            }

            pageOpened[player] = page
            player.openInventory(menu)
            openHandler?.invoke(this, menu, player)

            if (isCacheInventories && !pagesCache.containsKey(page)) {
                pagesCache[page] = menu
            }


            return menu
        }
        return null
    }

    override fun register(plugin: IPluginInstance) {
        registerMenu(plugin)
    }

    fun registerMenu(plugin: IPluginInstance) {
        registerListener(plugin.plugin as JavaPlugin)
        registeredMenus.add(this)
        for (button in buttons) {
            button.parentMenu = this
            if (button.isCategory) {
                button.menu?.superiorMenu = this
                button.menu?.register(plugin)
            }
        }
    }


    fun unregisterMenu() {
        unregisterListener()
        registeredMenus.remove(this)
        for (button in buttons) {
            if (button.isCategory) {
                button.menu?.unregisterMenu()
            }
        }

    }

    @EventHandler
    fun onClick(e: PlayerInteractEvent) {
        val p = e.player
        if (p.itemInHand == null)
            return

        if (openWithItem != null && Mine.equals(p.itemInHand, openWithItem)) {
            e.isCancelled = true
            open(p)
        }

    }

    @EventHandler
    fun onCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val message = event.message
        val cmd = Extra.getCommandName(message)
        openWithCommand ?: return

        if (cmd.toLowerCase() == openWithCommand!!.toLowerCase()) {
            event.isCancelled = true
            open(player)
        }
    }

    @EventHandler
    fun onClick(e: InventoryClickEvent) {

        if (e.whoClicked is Player) {

            val player = e.whoClicked as Player

            if (isOpen(player, e.inventory)) {
                debug("Nome do Menu: " + e.inventory.name)
                e.isCancelled = true
                val slot = e.rawSlot
                var page: Int = getPageOpen(player)

                val itemClicked = e.currentItem
                var button: MenuButton? = null
                if (itemClicked != null) {

                    if (previousPage.item == itemClicked) {
                        open(player, (--page))
                        return
                    } else if (nextPage.item == itemClicked) {
                        open(player, (++page))
                        return
                    } else if (backPage.item == itemClicked) {
                        if (superiorMenu != null) {
                            superiorMenu?.open(player)
                        }
                        return
                    } else {
                        button = getButton(itemClicked, player)
                        debug("Button by Item " + if (button == null) "is Null" else "is not null")
                    }
                }
                if (button == null) {
                    button = getButton(page, slot)
                    debug("Button by Slot " + if (button == null) "is Null" else "is not null")
                }

                if (button != null) {
                    debug("Button is not null")
                    button.click.accept(e)
                    if (button.effects != null) {
                        debug("Button make Editable Effects")
                        button.effects?.accept(player)
                    }
                    if (button.isCategory) {
                        button.menu?.open(player)
                        debug("Button open another menu")
                        return
                    }
                }
                if (effect != null) {
                    debug("Played menu effect")
                    effect?.accept(e)
                }
            }
        }

    }


    fun isOpen(player: Player): Boolean {
        return pageOpened.containsKey(player)
    }

    fun isOpen(player: Player, inventory: Inventory): Boolean {
        try {
            val holder = inventory.holder!!
            if (holder is FakeInventoryHolder) {
                if (holder.menu == this) return true

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return false
    }

    fun getPageOpen(player: Player): Int {
        return pageOpened.getOrDefault(player, 0)
    }

    companion object {
        var isDebug = true
        val registeredMenus = ArrayList<Menu>()


        fun debug(msg: String) {
            if (isDebug) {
                Mine.console("§b[Menu] §7$msg")
            }

        }

    }


}
