package net.eduard.api.lib.menu

import net.eduard.api.lib.game.SoundEffect
import java.util.ArrayList

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import net.eduard.api.lib.plugin.IPluginInstance
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception

fun menu(title: String, amount: Int, setup: Menu.() -> Unit): Menu {
    val menu = Menu(title, amount)
    setup.invoke(menu)
    return menu
}

fun shop(title: String, amount: Int, setup: Shop.() -> Unit): Shop {
    val menu = Shop(title, amount)
    setup.invoke(menu)
    return menu
}
fun Player.getMenu(): Menu? {
    try {
        if (openInventory == null)return null
        if (openInventory.topInventory == null)return null
        val holder = openInventory.topInventory.holder ?: return null
        if (holder is FakeInventoryHolder) {
            return holder.menu
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return null
}

/**
 * Sistema proprio de criacao de Menus Interativos automaticos para facilitar
 * sua vida
 *
 * @author Eduard
 */
@Suppress("unused")
open class Menu(

    var title: String = "Menu",
    var lineAmount: Int = 1

) : EventsManager() {
    @Transient
    var autoAlignPerLine = 7
    @Transient
    var autoAlignPerPage = autoAlignPerLine * 1

    fun button(name: String = "Botao", setup: (MenuButton.() -> Unit)): MenuButton {
        val button = MenuButton(name, null)
        button.parentMenu = this
        setup(button)
        addButton(button)
        return button
    }

    @Transient
    var itemsEffect: MenuItems<*>? = null

    fun <T : Any> items(
        setup: (MenuItems<T>.() -> Unit)
    ): MenuItems<T> {
        val listContent = MenuItems<T>()
        listContent.menu = this
        itemsEffect = listContent
        setup(listContent)
        return listContent
    }

    @Transient
    var lastInteraction = mutableMapOf<Player, Long>()

    open fun canInteract(player: Player): Boolean {

        if (player in lastInteraction) {
            val interactionMoment = lastInteraction[player]!!
            return System.currentTimeMillis() - interactionMoment > cooldownBetweenInteractions
        }
        return true
    }

    open fun interact(player: Player) = lastInteraction.put(player, System.currentTimeMillis())


    @Transient
    var superiorMenu: Menu? = null

    @Transient
    var titlePerPlayer: (Player.() -> String)? = null


    @Transient
    var openHandler: (Menu.(Inventory, Player) -> Unit)? = null

    @Transient
    var effect: ClickEffect? = null

    @Transient
    private var pagesCache = mutableMapOf<Int, Inventory>()

    @Transient
    private var inventoryCache = mutableMapOf<Player, Inventory>()


    @Transient
    private var buttonsCache = mutableMapOf<String, MenuButton>()

    @Transient
    private val pageOpened = mutableMapOf<Player, Int>()


    var pageAmount = 1
    var pagePrefix = ""
    var pageSuffix = "(\$page/\$max_page)"
    var showPage = false
    var isTranslateIcon = false
    var isAutoAlignItems = false
    var autoAlignSkipColumns = listOf(9, 1)
    var autoAlignSkipLines = listOf(1, lineAmount)
    var isCacheInventories = false
    var isPerPlayerInventory = false
    var closeMenuWhenButtonsCleared = false
    var cooldownBetweenInteractions = 1000L
    var openWithItem: ItemStack? = Mine.newItem(Material.COMPASS, "§aMenu Exemplo", 1, 0, "§2Clique abrir o menu")
    var openWithCommand: String? = null
    var openWithCommandText: String? = null
    var openNeedPermission: String? = null
    var messagePermission = "§cVocê precisa de permissão do Cargo Master para abrir este menu."

    var previousPage = Slot(
        Mine.newItem(Material.ARROW, "§aVoltar Página.", 1, 0, "§7Clique para ir para a página anterior."), 1, 1
    )
    var previousPageSound: SoundEffect? = null
    var nextPageSound: SoundEffect? = null
    var backPageSound: SoundEffect? = null

    fun enableSounds() {
        previousPageSound = SoundEffect(Sound.CLICK, 2f, 2f)
        nextPageSound = SoundEffect(Sound.CLICK, 2f, 2f)
        backPageSound = SoundEffect(Sound.CLICK, 2f, 2f)
    }


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
        openWithCommandText = null
    }


    val fullTitle: String = pagePrefix + title + pageSuffix


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

    val hasPages: Boolean
        get() = pageAmount > 1

    fun removeButton(name: String) {
        val button = getButton(name) ?: return
        buttons.remove(button)

    }

    fun removeAllButtons() {
        buttons.clear()
        clearCache()
        if (closeMenuWhenButtonsCleared)
            pageOpened.forEach { (p, _) -> p.closeInventory() }
        pageOpened.clear()
        lastPage = 1
        lastSlot = 9
        pageAmount = 1
    }

    open fun copy(): Menu {
        return Copyable.copyObject(this)
    }


    fun getButtonUsingStream(icon: ItemStack, player: Player): MenuButton? {

        val page = pageOpened[player] ?: 1
        val skipValue = autoAlignPerPage * page - 1
        val stream = buttons.stream().skip(skipValue.toLong())
            .filter { Mine.equals(it.getIcon(player), icon) }
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

    fun getButton(icon: ItemStack, player: Player?): MenuButton? {
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


    fun addButton(button: MenuButton) {
        buttons.add(button)
        button.parentMenu = this
        buttonsCache[button.name] = button
        if (!button.fixed && isAutoAlignItems) {
            nextPosition()
            button.page = lastPage
            button.index = lastSlot
        }
    }

    @Transient
    var lastPage = 1

    @Transient
    var lastSlot = 0
    val slotLimit
        get(): Int {
            return (lineAmount * 9) - 1
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
        lastSlot = 0
        lastPage = 1
        for (button in buttons) {
            if (button.fixed) continue
            nextPosition()
            button.index = lastSlot
            button.page = lastPage
        }
        pageAmount = lastPage
    }

    /**
     * Método usado para gerar paginação
     */
    fun nextPosition() {
        lastSlot++
        var antiForInfinito = 100
        while (needChangePosition()) {
            antiForInfinito--
            lastSlot++
            if (antiForInfinito==0)break
        }
        if (lastSlot > slotLimit) {
            pageAmount++
            lastPage++
            lastSlot=-1
            nextPosition()
        }

    }

    fun needChangePosition(): Boolean {
        for (line in autoAlignSkipLines) {
            if (Extra.getLine(lastSlot) == line)
                return true
        }
        for (column in autoAlignSkipColumns) {
            if (Mine.isColumn(lastSlot, column))
                return true
        }
        return false;
    }

    open fun open(player: Player, pageOpened: Int): Inventory? {
        if (!canOpen(player)) {
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
            this.pageOpened[player] = page
            player.openInventory(pagesCache[page])
        } else {
            if ((lineAmount < 1) or (lineAmount > 6)) {
                lineAmount = 1
            }

            val currentTitle = titlePerPlayer?.invoke(player) ?: title
            val prefix = pagePrefix.replace("\$max_page", "" + pageAmount)
                .replace("\$page", "" + page)
            val suffix = pageSuffix.replace("\$max_page", "" + pageAmount)
                .replace("\$page", "" + page)
            val menuTitle = Extra.cutText(
                if (showPage) {
                    prefix + currentTitle + suffix
                } else currentTitle, 32
            )

            val fakeHolder = FakeInventoryHolder(this)
            val menu = Bukkit.createInventory(fakeHolder, 9 * lineAmount, menuTitle)
            fakeHolder.openInventory = menu
            fakeHolder.pageOpenned = page
            update(menu, player, page)

            player.openInventory(menu)
            if (isCacheInventories && !pagesCache.containsKey(page)) {
                pagesCache[page] = menu
            }


            return menu
        }
        return null
    }

    open fun canOpen(player: Player): Boolean {
        return true
    }

    open fun update() {

    }

    open fun update(menu: Inventory, player: Player, page: Int = 1) {
        menu.clear()
        if (hasPages) {
            if (page > 1) {
                val itemPageBack = Mine.applyPlaceholders(
                    previousPage.item!!.clone(),
                    mapOf(
                        "%page" to "" + (page - 1),
                    )
                )
                menu.setItem(previousPage.slot, itemPageBack)

            }
            if (page < pageAmount) {
                val itemPageNext = Mine.applyPlaceholders(
                    nextPage.item!!.clone(),
                    mapOf(
                        "%page" to "" + (page + 1),
                    )
                )
                menu.setItem(nextPage.slot, itemPageNext)
            }
        }
        if (this.superiorMenu != null) {
            backPage.give(menu)
        }
        itemsEffect?.update(menu, player)
        for (button in buttons) {
            if (!button.fixed) {
                if (button.index > slotLimit) {
                    button.index = 0
                }
                if (button.page != page) continue
            }
            button.updateButton(menu, player)

        }
        this.pageOpened[player] = page
        openHandler?.invoke(this, menu, player)
    }


    override fun register(plugin: IPluginInstance) {
        registerMenu(plugin)
    }

    open fun registerMenu(plugin: IPluginInstance) {
        var id = 1
        val namesUsed = mutableSetOf<String>()
        for (button in buttons) {
            val lastEquals = (namesUsed.contains(button.name))
            if (button.name == "Produto" || lastEquals) {
                button.name = "Produto-$id"
            }
            if (button.name == "Botao" || lastEquals) {
                button.name = "Botao-$id"
            }
            if (!lastEquals) {
                namesUsed.add(button.name)
            }
            id++
        }
        registerListener(plugin.plugin as JavaPlugin)
        registeredMenus.add(this)
        for (button in buttons) {
            buttonsCache[button.name] = button
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
    fun eventOnPlayerClick(e: PlayerInteractEvent) {
        val player = e.player
        if (player.itemInHand == null)
            return
        if (e.action == Action.PHYSICAL) return
        if (openWithItem == null) return
        if (!Mine.equals(player.itemInHand, openWithItem)) return
        e.isCancelled = true
        open(player)
    }

    @EventHandler
    fun eventOnCommand(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val message = event.message
        val cmd = Extra.getCommandName(message)
        openWithCommand ?: return
        if (cmd.equals(openWithCommand!!, true)) {
            event.isCancelled = true
            open(player)
        }
    }

    @EventHandler
    fun eventOnCommandText(event: PlayerCommandPreprocessEvent) {
        val player = event.player
        val message = event.message
        openWithCommandText ?: return
        if (message.toLowerCase().startsWith(openWithCommandText!!.toLowerCase())) {
            event.isCancelled = true
            open(player)
        }
    }

    fun Player.goBack() {
        lastOpennedMenu[this]?.open(this)
    }

    fun Player.getLastMenu(): Menu {
        return lastOpennedMenu[this]!!
    }

    @EventHandler
    fun eventOnClose(event: InventoryCloseEvent) {
        if (event.player !is Player) return
        val player = event.player as Player
        if (isOpen(player, event.inventory)) {
            lastOpennedMenu[player] = this
        }
    }

    @EventHandler
    fun eventOnClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return
        val player = event.whoClicked as Player
        if (!isOpen(player, event.inventory)) return
        debug("Nome do Menu: " + event.inventory.name)
        event.isCancelled = true
        val slot = event.rawSlot
        var page: Int = getPageOpen(player)
        val itemClicked = event.currentItem
        var button: MenuButton? = null
        if (itemClicked != null) {
            if (previousPage.slot == slot && page > 1) {
                previousPageSound?.create(player)
                open(player, (--page))
                return
            } else if (nextPage.slot == slot && page < pageAmount) {
                nextPageSound?.create(player)
                open(player, (++page))
                return
            } else if (backPage.slot == slot && superiorMenu != null) {
                backPageSound?.create(player)
                superiorMenu?.open(player)
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
        if (itemsEffect != null) {
            itemsEffect!!.click.accept(event)
        }
        if (button != null) {
            if (!canInteract(player)) {
                debug("Button Interaction in cooldown")
                return
            }
            if (button.click != null) {
                interact(player)
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
        val lastOpennedMenu = mutableMapOf<Player, Menu>()

        fun debug(msg: String) {
            if (isDebug) {
                Mine.console("§b[Menu] §7$msg")
            }
        }
    }

}
