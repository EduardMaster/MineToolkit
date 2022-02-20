package net.eduard.api.lib.menu

import net.eduard.api.lib.abstraction.Minecraft_v1_8_R3
import net.eduard.api.lib.game.SoundEffect

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
import net.eduard.api.lib.storage.annotations.StorageIndex
import org.bukkit.Sound
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import java.util.function.Consumer
import java.util.function.Function


inline fun menu(title: String, lineAmount: Int, setup: Menu.() -> Unit): Menu {
    val menu = Menu(title, lineAmount)
    setup.invoke(menu)
    return menu
}


inline fun shop(title: String, lineAmount: Int, setup: Shop.() -> Unit): Shop {
    val menu = Shop(title, lineAmount)
    setup.invoke(menu)
    return menu
}

fun Player.getMenu(): Menu? {
    try {

        if (openInventory == null) return null
        if (openInventory.topInventory == null) return null
        // se não for bau custom não verifica o holder
        if (openInventory.type != InventoryType.CHEST) return null
        try {
            val holder = openInventory.topInventory.holder ?: return null
            if (holder is FakeInventoryHolder) {
                return holder.menu
            }
        } catch (ex: IllegalStateException) {
            // causa esse erro na versão 1.16.5 por tentar acessar o Holder usando Async
        } catch (ex: RuntimeException) {
            // causa esse erro na versão 1.16.5 por tentar acessar o Holder usando Async
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
    var title: String,
    var lineAmount: Int
) : EventsManager() {

    constructor(title: String) : this(title, 3)
    constructor() : this("Menu Vazio", 6)


    @StorageIndex
    var index = title.toLowerCase().replace(" ", "_")

    @Transient
    var autoAlignPerLine = 7

    @Transient
    var autoAlignPerPage = autoAlignPerLine * 1

    @Transient
    var lastInteraction = mutableMapOf<Player, Long>()

    @Transient
    var superiorMenu: Menu? = null


    @Transient
    var titleGenerated: Function<Player, String>? = null

    var titlePerPlayer: (Player.() -> String)?
        set(value) {
            titleGenerated = Function(value!!)
        }
        get() = null


    @Transient
    var lineAmountPerPlayer: (Player.() -> Int)? = null

    @Transient
    var openEffect: OpenEffect? = null

    var openHandler: (Menu.(Inventory, Player) -> Unit)?
        set(value) {
            openEffect = OpenEffect { player, inventory ->
                value!!.invoke(this, inventory, player)
            }
        }
        get() {
            return null
        }

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
    var pageSuffix = "(%page/%max_page)"
    var showPage = false
    var isPerPlayer = false
    var isTranslateIcon = false
    var isAutoAlignItems = false
    var autoAlignSkipColumns = listOf(9, 1)
    var autoAlignSkipLines = listOf(1, lineAmount)
    var isCacheInventories = false
    var closeMenuWhenButtonsCleared = false
    var rebuildMenuWhenOpen = false
    var cooldownBetweenInteractions = 250L
    var openWithItem: ItemStack? = null
        get() {
            if (field != null && openIconDefault.equals(field)){
                field = null
            }
                return field
        }

    var openWithCommand: String? = null
    var openWithCommandText: String? = null
    var openNeedPermission: String? = null
    var messagePermission = "§cVocê precisa de permissão do Cargo Master para abrir este menu."


    fun isEmpty(): Boolean {
        return buttons.isEmpty()
    }

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
        Mine.newItem(
            Material.ARROW,
            "§aVoltar para Menu Principal.",
            1,
            0,
            "§7Clique para ir para a página superior."
        ), 1, 3
    )
    var nextPage =
        Slot(Mine.newItem(Material.ARROW, "§aPróxima Página.", 1, 0, "§7Clique para ir para a próxima página."), 9, 1)
    var buttons = mutableListOf<MenuButton>()

    @Transient
    val playersMenu = mutableMapOf<Player, Menu>()

    @Transient
    var buildByPlayer: (Menu.(Player) -> Unit)? = null


    fun getMenu(player: Player): Menu {
        isPerPlayer = true
        if (player in playersMenu) {
            return playersMenu[player]!!
        }
        val menu = Menu()
        menu.title = title
        menu.backPage = backPage
        menu.titleGenerated = titleGenerated
        menu.isAutoAlignItems = isAutoAlignItems
        menu.isCacheInventories = isCacheInventories
        menu.rebuildMenuWhenOpen = true
        menu.closeMenuWhenButtonsCleared = closeMenuWhenButtonsCleared
        menu.buildByPlayer = buildByPlayer
        menu.isPerPlayer = false
        menu.superiorMenu = superiorMenu
        menu.registerJavaMenu(plugin)
        playersMenu[player] = menu
        return menu
    }

    fun cantBeOpened() {
        openWithItem = null
        openWithCommand = null
        openWithCommandText = null
    }

    inline fun button(name: String = "Botao", kotlinLambda: (MenuButton.() -> Unit)): MenuButton {
        val button = MenuButton(name)
        kotlinLambda(button)
        addButton(button)
        return button
    }

    fun button(name: String, consumer: Consumer<MenuButton>): MenuButton {
        val button = MenuButton(name)
        consumer.accept(button)
        addButton(button)
        return button
    }


    open fun menuPlayerCanInteract(player: Player): Boolean {
        if (player in lastInteraction) {
            val interactionMoment = lastInteraction[player]!!
            return System.currentTimeMillis() - interactionMoment > cooldownBetweenInteractions
        }
        return true
    }

    open fun menuPlayerInteract(player: Player) = lastInteraction.put(player, System.currentTimeMillis())


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

    fun removeButton(button: MenuButton) {
        buttons.remove(button)
        if (this is Shop) {
            organize()
        }
        if (isCacheInventories) return
        for (player in Mine.getPlayers()) {
            val menu = player.getMenu() ?: continue
            if (menu != this) continue
            val pagina = menu.getPageOpen(player)
            val inventory = player.openInventory.topInventory
            menu.update(inventory, player, pagina)
        }
    }

    fun removeButton(name: String) {
        val button = getButton(name) ?: return
        removeButton(button)
    }

    fun removeAllButtons() {
        for (button in buttons) {
            if (button.isCategory) {
                button.menu?.removeAllButtons()
                button.menu?.unregisterMenu()
                //button.menuLink?.unregisterMenu()
            }
        }
        clearCache()
        buttons.clear()
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
        if (button.isCategory && isRegistered) {
            button.menu?.superiorMenu = this
            button.menuLink?.superiorMenu = this
            button.menu?.registerJavaMenu(plugin)
            button.menuLink?.registerJavaMenu(plugin)
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

    /**
     * Remove varias informações em Cache
     */
    fun clearCache() {

        pagesCache.clear()
        if (closeMenuWhenButtonsCleared)
            pageOpened.forEach { (p, _) -> p.closeInventory() }
        pageOpened.clear()
        for (menu in playersMenu.values) {
            menu.clearCache()
        }
        playersMenu.clear()
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

    /**
     * Abre a página 1 para o Jogador
     * @param player Jogador
     */
    fun open(player: Player): Inventory? {
        return open(player, 1)
    }

    fun updateButtonPositions() {
        updateButtonPositions(buttons)
    }

    fun updateButtonPositions(collection: Collection<MenuButton>) {
        lastSlot = 0
        lastPage = 1
        for (button in collection) {
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
        val maxSkips = 3 * 9
        var antiForInfinito = maxSkips
        while (needChangePosition()) {
            antiForInfinito--
            lastSlot++
            if (antiForInfinito == 0) break
        }
        if (lastSlot > slotLimit) {
            pageAmount++
            lastPage++
            lastSlot = 0
            antiForInfinito = maxSkips
            while (needChangePosition()) {
                antiForInfinito--
                lastSlot++
                if (antiForInfinito == 0) {
                    lastPage = 0
                    break
                }
            }
        }

    }

    /**
     * Verifica se precisa avançar para um Próximo Slot que não esteja ocupado ou bloqueado
     */
    fun needChangePosition(): Boolean {
        for (line in autoAlignSkipLines) {
            if (Extra.getLine(lastSlot) == line)
                return true
        }
        for (column in autoAlignSkipColumns) {
            if (Extra.isColumn(lastSlot, column))
                return true
        }
        return false;
    }

    /**
     * Abre o Menu em uma Página especifica para o Jogador
     * @param pageOpened Página Numero
     * @param player Jogador
     */
    open fun open(player: Player, pageOpened: Int): Inventory? {
        if (isPerPlayer && buildByPlayer != null) {
            return getMenu(player).open(player)
        }
        if (rebuildMenuWhenOpen) {
            update()
            buildByPlayer?.invoke(this, player)
        }
        if (!canOpen(player)) {
            return null
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
            var customLineAmount = lineAmount
            if (lineAmountPerPlayer != null) {
                customLineAmount = lineAmountPerPlayer!!(player)
            }
            if ((customLineAmount < 1) or (customLineAmount > 6)) {
                customLineAmount = 1
            }
            val currentTitle = titleGenerated?.apply(player) ?: title
            val prefix = pagePrefix.replace(
                "%max_page", "" +
                        pageAmount
            )
                .replace("%page", "" + page, false)
                .replace("\$page", "" + page, false)
            val suffix = pageSuffix.replace("%max_page", "" + pageAmount)
                .replace("%page", "" + page, false)
                .replace("\$page", "" + page, false)

            val menuTitle = Extra.cutText(
                if (showPage) {
                    prefix + currentTitle + suffix
                } else currentTitle, 32
            )

            val fakeHolder = FakeInventoryHolder(this)
            val menu = Bukkit.createInventory(
                fakeHolder, 9 * customLineAmount,

                menuTitle
            )
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

    /**
     * Se o menu pode ser aberto por um Jogador
     * @param player Jogador
     */
    open fun canOpen(player: Player): Boolean {
        if (openNeedPermission != null) {
            if (!player.hasPermission(openNeedPermission)) {
                player.sendMessage(messagePermission)
                return false
            }
        }
        return true
    }

    /**
     * Remoção e Atualização dos botões do menu
     */
    open fun update() {

    }

    open fun update(menu: Inventory, player: Player) {
        update(menu, player, 1)
    }

    open fun update(menu: Inventory, player: Player, page: Int) {
        update(menu, player, page, true)
    }

    /**
     * Atualiza o Inventário com os ItemStack dos MenuButton da página espeficiada
     * @param menu Inventário
     * @param player Jogador
     * @param page Página
     */
    open fun update(menu: Inventory, player: Player, page: Int, clearInventory: Boolean) {
        if (clearInventory)
            menu.clear()
        if (hasPages) {
            if (page > 1) {
                val itemPageBack = Mine.applyPlaceholders(
                    previousPage.item!!.clone(),
                    mapOf(
                        "%page" to "" + (page - 1),
                        "\$page" to "" + (page - 1),
                    )
                )
                menu.setItem(previousPage.slot, itemPageBack)

            }
            if (page < pageAmount) {
                val itemPageNext = Mine.applyPlaceholders(
                    nextPage.item!!.clone(),
                    mapOf(
                        "%page" to "" + (page + 1),
                        "\$page" to "" + (page - 1),
                    )
                )
                menu.setItem(nextPage.slot, itemPageNext)
            }
        }
        if (this.superiorMenu != null) {
            backPage.give(menu)
        }

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
        openEffect?.accept(player, menu)
    }

    /**
     * Alias para Menu#registerMenu(plugin)
     * @param plugin Plugin
     */
    override fun register(plugin: IPluginInstance) {
        registerMenu(plugin)
    }

    /**
     * Registra o Listener deste menu e de seus submenus e configura nomes de botões sem nomes
     */
    open fun registerMenu(pluginInstance: IPluginInstance) {
        registerJavaMenu(pluginInstance.plugin as JavaPlugin)
    }

    /**
     * Registra o Listener deste menu e de seus submenus e configura nomes de botões sem nomes
     */
    open fun registerJavaMenu(plugin: JavaPlugin) {
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
        registerListener(plugin)
        registeredMenus.add(this)
        for (button in buttons) {
            buttonsCache[button.name] = button
            button.parentMenu = this
            if (button.isCategory) {
                button.menu?.superiorMenu = this
                button.menuLink?.superiorMenu = this
                button.menu?.registerJavaMenu(plugin)
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
        buttons.clear()
    }

    @EventHandler
    fun eventOnPlayerClick(e: PlayerInteractEvent) {
        val player = e.player
        if (player.itemInHand == null)
            return
        if (e.action == Action.PHYSICAL) return
        if (openWithItem == null) return
        if (openWithItem!!.isSimilar(player.itemInHand)) {
            e.isCancelled = true
            open(player)
        }

    }

    @EventHandler(ignoreCancelled = true)
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

    @EventHandler(ignoreCancelled = true)
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
    fun eventOnQuit(event: PlayerQuitEvent) {
        val player = event.player
        lastOpennedMenu.remove(player)
        this.lastInteraction.remove(player)
        this.inventoryCache.remove(player)
        this.playersMenu.remove(player)
    }


    @EventHandler
    fun eventOnClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) return
        val player = event.whoClicked as Player
        if (!isOpen(player, event.inventory)) return
        debug("Nome do Menu: " + event.view.title)
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

        if (button != null) {
            if (!menuPlayerCanInteract(player)) {
                debug("Button Interaction in cooldown")
                return
            }
            if (button.click != null) {
                menuPlayerInteract(player)
                debug("Button make click effect")
                button.click?.accept(event)
            }
            if (button.effects != null) {
                debug("Button make Editable Effects")
                button.effects?.playEffects(player)
            }
            if (button.isCategory) {
                button.menu?.open(player)
                button.menuLink?.open(player)
                debug("Button open another menu")
                return
            }
        }
        if (effect != null) {
            debug("Played menu effect")
            effect?.accept(event)
        }
    }


    /**
     * Verifica se o jogador esta com menu aberto
     */
    fun isOpen(player: Player): Boolean {
        return pageOpened.containsKey(player)
    }


    /**
     * Verifica se o jogador abriu este Inventario e se ele é este Menu
     * @param player Jogador
     * @param inventory Inventario
     */
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

    /**
     * Pega a Página aberta
     * @return Numero
     */
    fun getPageOpen(player: Player): Int {
        return pageOpened.getOrDefault(player, 0)
    }

    companion object {
        var isDebug = true
        val registeredMenus = ArrayList<Menu>()
        val lastOpennedMenu = mutableMapOf<Player, Menu>()
        val openIconDefault = Mine.newItem(Material.COMPASS, "§aMenu Exemplo",
        1, 0, "§2Clique abrir o menu")
        fun debug(msg: String) {
            Minecraft_v1_8_R3
            if (isDebug) {
                Mine.console("§b[Menu] §7$msg")
            }
        }
    }

}
