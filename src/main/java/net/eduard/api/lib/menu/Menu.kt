package net.eduard.api.lib.menu

import java.util.ArrayList
import java.util.HashMap

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.MineReflect
import net.eduard.api.lib.plugin.IPluginInstance
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception

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

    constructor(
        title: String,
        lineAmount: Int
    ) : this(title, lineAmount, null)

    fun button(name: String = "Botao", block: (MenuButton.() -> Unit)? = null): MenuButton {
        return MenuButton(name, this, block = block)

    }


    @Transient
    val playersWhoCanOpen = mutableListOf<Player>()

    fun canOpen(player : Player): Boolean {
        if (playersWhoCanOpen.isEmpty())return true
        return playersWhoCanOpen.contains(player)
    }


    @Transient
    var superiorMenu: Menu? = null

    @Deprecated("Use multi button instead")
    @Transient
    var openHandler: (Menu.(Inventory, Player) -> Unit)? = null

    @Transient
    var effect: ClickEffect? = null

    @Transient
    private var pagesCache: MutableMap<Int, Inventory> = HashMap()

    @Transient
    private var inventoryCache: MutableMap<Player, Inventory> = HashMap()


    @Transient
    private var buttonsCache = mutableMapOf<String, MenuButton>()

    @Transient
    private val pageOpened = HashMap<Player, Int>()

    var pageAmount = 1
    var pagePrefix = ""
    var pageSuffix = "(\$page/\$max_page)"

    var isTranslateIcon: Boolean = false
    var isAutoAlignItems: Boolean = false
    var autoAlignSkipCenter = false
    var isCacheInventories: Boolean = false
    var isPerPlayerInventory = false
    var openWithItem: ItemStack? = Mine.newItem(Material.COMPASS, "§aMenu Exemplo", 1, 0, "§2Clique abrir o menu")
    var openWithCommand: String? = null
    var openNeedPermission: String? = null
    var messagePermission = "§cVocê precisa de permissão do Cargo Master para abrir este menu."
    var previousPage = Slot(
        Mine.newItem(Material.ARROW, "§aVoltar Página.", 1, 0, "§7Clique para ir para a página anterior."), 1, 1
    )
    var backPage = Slot(
        Mine.newItem(Material.ARROW, "§aVoltar para Menu Principal.", 1, 0, "§7Clique para ir para a página superior."),
        1,
        3
    )
    var nextPage = Slot(
        Mine.newItem(Material.ARROW, "§aPróxima Página.", 1, 0, "§7Clique para ir para a próxima página."), 9, 1
    )
    var buttons = mutableListOf<MenuButton>()


    fun cantBeOpened() {
        openWithItem = null
        openWithCommand = null
    }


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


    fun getButtonUsingStream(icon: ItemStack, player: Player): MenuButton? {
        val page = pageOpened[player] ?: 1
        val perPageItems = pageAmount - 1 * 7
        val skipValue = perPageItems * page - 1
        val stream = buttons.stream().skip(skipValue.toLong()).filter { Mine.equals(it.getIcon(player), icon) }
            .findFirst()
        if (stream.isPresent)
            return stream.get()
        return null
    }

    fun getButtonUsingFor(icon: ItemStack, player: Player): MenuButton? {
        val tempoAntes = System.currentTimeMillis()

        for (button in buttons) {
            if (Mine.equals(button.getIcon(player), icon)) {

                val tempoDepois = System.currentTimeMillis()
                debug("§aTempo para percorrer todos items " + (tempoDepois - tempoAntes))
                return button
            }
        }
        return null
    }

    fun getButton(icon: ItemStack, player: Player): MenuButton? {
        val data = MineReflect.getData(icon)
        val buttonName = data.getString("button-name")
        return buttonsCache[buttonName]
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

    fun getSlotInicial(): Int {
        if (isAutoAlignItems) {
            if (lineAmount > 1) {
                return 10
            }
        }
        return 0
    }

    fun getSlotLimit(): Int {
        if (isAutoAlignItems) {
            if (lineAmount > 2) {
                return ((lineAmount - 1) * 9) -1
            }
        }
        return (lineAmount * 9) - 1
    }


    fun addButton(button: MenuButton) {

        buttons.add(button)
        button.parentMenu = this

        if (button.item != null) {
            buttonsCache[button.name] = button
        }
        if (isAutoAlignItems) {
            nextPosition()
            button.page = lastPage
            button.index = lastSlot
        }
    }
    @Transient
    var lastPage = 1

    @Transient
    var lastSlot = getSlotInicial()

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
        for (slot in 0..(lineAmount * 9)) {
            getButton(page, slot) ?: return slot
        }

        return -1
    }

    fun isFull(page: Int): Boolean {

        for (slot in 0..(lineAmount * 9)) {
            getButton(page, slot) ?: return false
        }
        return true
    }

    fun open(player: Player): Inventory? {
        return open(player, 1)
    }

    fun updateButtonPositions() {

        lastSlot = getSlotInicial() - 1
        lastPage = 1

        for (button in buttons) {
            nextPosition()
            button.index = lastSlot
            button.page = lastPage
        }
        pageAmount = lastPage
    }

    /**
     * Método usado para gerar a nova posição do botão apartir
     * de um sistema de paginação com 7 items por linha
     * com 4 linhas com items e duas linhas vazias, e também com a
     * possibildade de deixar o meio sem items
     */
    fun nextPosition(){
        lastSlot++
        if (lastSlot < getSlotInicial()){
            lastSlot = getSlotInicial()
        }
        if (autoAlignSkipCenter) {
            if (Mine.isColumn(lastSlot, 5)) {
                lastSlot++
            }
        }
        if (Mine.isColumn(lastSlot, 9)) {
            lastSlot++
        }
        if (Mine.isColumn(lastSlot, 1)) {
            lastSlot++
        }
        if (lastSlot >= getSlotLimit()){
            lastPage++
            pageAmount++
            lastSlot = getSlotInicial()
        }
    }

    open fun open(player: Player, pageOpened: Int): Inventory? {
        if (!canOpen(player)){
            return null
        }
        if (openNeedPermission != null) {
            if (!player.hasPermission(openNeedPermission)) {
                player.sendMessage(messagePermission)
                return null
            }
        }
        var page = pageOpened
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


            val prefix = pagePrefix.replace("\$max_page", "" + pageAmount).replace("\$page", "" + page)
            val suffix = pageSuffix.replace("\$max_page", "" + pageAmount).replace("\$page", "" + page)
            var menuTitle = Extra.cutText(prefix + title + suffix, 32)
            if (!isPageSystem) {
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
            update(menu, player,page)


            this.pageOpened[player] = page
            player.openInventory(menu)
            openHandler?.invoke(this, menu, player)

            if (isCacheInventories && !pagesCache.containsKey(page)) {
                pagesCache[page] = menu
            }


            return menu
        }
        return null
    }
    fun update(menu:Inventory , player : Player , page : Int){
        for (button in buttons) {
            if (button.page != page) continue
            var position = button.index

            if (position >= getSlotLimit()) {
                position = 0
            }
            var icon = button.getIcon(player)
            if (isTranslateIcon) {
                icon = Mine.getReplacers(icon, player)
            }
            val data = MineReflect.getData(icon)
            data.setString("button-name", button.name)
            icon = MineReflect.setData(icon,data)
            menu.setItem(position, icon)
        }
    }


    override fun register(plugin: IPluginInstance) {
        registerMenu(plugin)
    }

    open fun registerMenu(plugin: IPluginInstance) {
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
        val player = e.player
        if (player.itemInHand == null)
            return

        if (openWithItem != null && Mine.equals(player.itemInHand, openWithItem)) {
            e.isCancelled = true
            open(player)
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
    fun onClick(event: InventoryClickEvent) {
        if (event.whoClicked is Player) {
            val player = event.whoClicked as Player
            if (isOpen(player, event.inventory)) {
                debug("Nome do Menu: " + event.inventory.name)
                event.isCancelled = true
                val slot = event.rawSlot
                var page: Int = getPageOpen(player)
                val itemClicked = event.currentItem
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
                    if (button.click != null) {
                        debug("Button make click effect")
                        button.click?.accept(event)
                    }
                    if (button.effects != null) {
                        debug("Button make Editable Effects")
                        button.effects?.playEffects(player)
                    }

                    if (button.isCategory) {
                        button.menu?.open(player)
                        debug("Button open another menu")
                        return
                    }
                }
                if (effect != null) {
                    debug("Played menu effect")
                    effect?.accept(event)
                }
            }
        }
    }


    fun isOpen(player: Player): Boolean {
        return pageOpened.containsKey(player)
    }


    fun isOpen(player: Player, inventory: Inventory): Boolean {
        try {
            val holder = inventory.holder ?: return false
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
