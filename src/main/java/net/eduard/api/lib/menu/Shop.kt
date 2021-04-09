package net.eduard.api.lib.menu

import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.kotlin.player
import net.eduard.api.server.currency.CurrencyManager
import net.eduard.api.lib.modules.*
import net.eduard.api.lib.plugin.IPluginInstance
import net.eduard.api.lib.storage.annotations.StorageAttributes

import net.eduard.api.server.CurrencySystem
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.math.abs

@Suppress("unused")
@StorageAttributes(indentificate = true)
open class Shop(
    name: String = "Loja",
    lineAmount: Int = 3
) : Menu(name, lineAmount) {


    fun product(name: String = "Produto", setup: (Product.() -> Unit)): Product {
        val product = Product(name, this)
        setup.invoke(product)
        return product
    }


    var currencyType = "VaultEconomy"

    @Transient
    var currency: CurrencySystem? = null
        set(value) {
            field = value
            for (button in buttons) {
                if (button.isCategory && button.menu is Shop) {
                    val shop = button.shop
                    shop.currency = value
                }
            }
        }
        get() {
            if (field == null) {
                field = CurrencyManager.getCurrency(currencyType)
            }
            return field

        }
    var sortType = ShopSortType.BUY_PRICE_ASC
    var menuUpgrades: Menu? = null
    var menuConfirmation: Menu? = null

    @Transient
    var selectingAmount = mutableMapOf<Player, Product>()

    @Transient
    var trading = mutableMapOf<Player, TradeType>()

    @Transient
    val selectedProduct = mutableMapOf<Player, Product>()

    var isAmountPerChat = false
    var isPermissionShop = false
    var glowIconWhenAlreadyBought = true
    var buyTemplate = TEMPLATE_BUY.toMutableList()
    val boughtTemplate = TEMPLATE_BOUGHT.toMutableList()
    var sellTemplate = TEMPLATE_SELL.toMutableList()
    var sellBuyTemplate = TEMPLATE_BUY_SELL.toMutableList()

    var messageChoiceAmount = MESSAGE_CHOICE_AMOUNT
    var messageBoughtItem = MESSAGE_BOUGHT_ITEM
    var messageSoldItem = MESSAGE_SOLD_ITEM
    var messageAlreadyBought = MESSAGE_ALREADY_BOUGHT
    var messageWithoutItems = MESSAGE_WITHOUT_ITEMS
    var messageWithoutBalance = MESSAGE_WITHOUT_BALANCE
    var messageWithoutPermission = MESSAGE_WITHOUT_PERMISSION
    var messageUpgradeBought = MESSAGE_UPGRADE_BOUGHT
    var messageUpgradesAlreadyBought = MESSAGE_UPGRADES_ALREADY_BOUGHT


    companion object {
        var MESSAGE_CHOICE_AMOUNT = "§aEscolha uma quantidade para \$trader este produto \$product"
        var MESSAGE_BOUGHT_ITEM = "§aVoce adquiriu \$amount (\$product) produto(s) da Loja!"
        var MESSAGE_SOLD_ITEM = "§aVocê vendeu \$amount (\$product) produtos(s) para a loja!"
        var MESSAGE_WITHOUT_ITEMS = "§cVoce não tem items suficiente!"
        var MESSAGE_WITHOUT_BALANCE = "§cVoce não tem dinheiro suficiente!"
        var MESSAGE_WITHOUT_PERMISSION = "§cVoce não tem permissão para comprar este produto!"
        var MESSAGE_ALREADY_BOUGHT = "§cVocê já comprou este produto."
        var MESSAGE_UPGRADE_BOUGHT = "§cVocê comprou evolução do \$product para o nível \$level"
        var MESSAGE_UPGRADES_ALREADY_BOUGHT = "§cTodos os upgrades já foram adquiridos."


        var TEMPLATE_BUY = listOf(
            "§e\$product_name",
            "",
            "§7Valor unitário: §a\$product_buy_unit_price",
            "§7Valor do Pack: §a\$product_buy_pack_price",
            "§7Quantidade em Estoque: §b\$product_stock"
        )
        var TEMPLATE_BOUGHT = listOf(
            "",
            "§a§lDESBLOQUEADO"
        )
        var TEMPLATE_SELL = listOf(
            "§e\$product_name",
            "",
            "§7Valor do Pack: §a\$product_sell_pack_price",
            "§7Valor do Inventário: §a\$product_sell_inventory_price",
            "§7Quantidade em Estoque: §b\$product_stock"
        )
        var TEMPLATE_BUY_SELL = listOf(
            "§e\$product_name",
            "",
            "§cComprar",
            " §7Custo x1: §a\$product_buy_unit_price",
            " §7Custo x64: §a\$product_buy_pack_price",
            "",
            "§cVender",
            " §7Custo do Pack: §a\$product_sell_pack_price",
            " §7Custo do Inventário: §a\$product_sell_inventory_price",
            "",
            "§7Quantidade em Estoque: §b\$product_stock",


            )
        const val PLAYER_INVENTORY_LIMIT = 4 * 64 * 9
    }


    fun useUpgradesMenu() {
        menuUpgrades = menu("Lista de Upgrades", 6) {
            cantBeOpened()

            button("upgrade") {
                setPosition(3, 2)
                icon = ItemBuilder(Material.ANVIL).name("§aEvoluir")
            }


            button("product") {
                setPosition(5, 2)
                icon = ItemBuilder(Material.STONE)
            }

        }
    }

    fun useConfirmationMenu() {
        this.menuConfirmation = menu("Confirmar Transação", 3) {
            cantBeOpened()
            button("confirmar") {
                setPosition(3, 2)
                icon = ItemBuilder(Material.WOOL)
                    .data(5)
                    .name("§a§lCONFIRMAR")
                    .lore("§aClique para confirmar a transação.")
            }

            button("cancelar") {
                icon = ItemBuilder(Material.WOOL)
                    .data(14)
                    .name("§c§lCANCELAR")
                    .lore("§cClique para cancelar a transação.")
                setPosition(7, 2)
            }

            button("product") {
                setPosition(5, 2)
                icon = ItemBuilder(Material.STONE)
            }
        }

    }


    override fun copy(): Shop {
        return Copyable.copyObject(this)
    }

    fun organize() {
        if (sortType === ShopSortType.BUY_PRICE_ASC) {

            val result = buttons.filterIsInstance<Product>().sortedBy { it.buyPrice }
            for ((index, data) in result.withIndex()) {
                data.index = index
            }

        }
    }

    @EventHandler
    fun chat(e: AsyncPlayerChatEvent) {
        val player = e.player
        if (selectingAmount.containsKey(player)) {
            val product = selectingAmount[player]!!
            var amount = Extra.fromMoneyToDouble(e.message)
            amount = abs(amount)
            selectingAmount.remove(player)
            val trade = trading[player]
            trading.remove(player)
            if (trade === TradeType.BUYABLE) {
                buy(player, product, amount)
            } else if (trade === TradeType.SELABLE) {
                sell(player, product, amount)
            }
            e.isCancelled = true
        }
    }

    fun buy(player: Player, product: Product, value: Double) {
        var amount = value
        val fake = FakePlayer(player)
        val priceUnit = product.unitBuyPrice
        if (product.isLimited && amount > product.stock) {
            amount = product.stock
        }
        if (isPermissionShop) {
            if (player.hasPermission(product.permission)) {
                player.sendMessage(messageAlreadyBought)
                return
            }
        } else {
            if (!player.hasPermission(product.permission)) {
                player.sendMessage(messageWithoutPermission)
                return
            }
        }
        val priceFinal = priceUnit * amount
        val evento = ProductTradeEvent(player)
        evento.product = product
        evento.amount = amount
        if (product.isLimited) {
            evento.newStock = product.stock - amount
        } else {
            evento.newStock = product.stock
        }
        evento.balance = currency!!.get(fake)
        evento.type = TradeType.BUYABLE
        evento.priceTotal = priceFinal
        evento.shop = this@Shop
        Mine.callEvent(evento)
        if (evento.isCancelled) {
            debug("Cancelando a troca")
            return
        }
        if (currency!!.contains(fake, evento.priceTotal)) {
            currency!!.remove(fake, evento.priceTotal)
        } else {
            player.sendMessage(messageWithoutBalance)
            return
        }
        product.stock = evento.newStock
        for (cmd in product.commands) {
            val cmdExecuted =
                cmd.replace("\$player", player.name)
                    .replace("\$formated_amount", Extra.formatMoney(amount))
                    .replace("\$amount", "" + amount)
            debug("CMD: $cmdExecuted")
            Mine.runCommand(cmdExecuted)
        }
        player.sendMessage(
            messageBoughtItem
                .replace(
                    "\$amount",
                    Extra.formatMoney(amount)
                ).replace(
                    "\$product",
                    "" + product.name
                )
        )
        if (isPermissionShop) {
            if (VaultAPI.hasVault() && VaultAPI.hasPermission()) {
                VaultAPI.getPermission().playerAdd(null, fake, product.permission)
            }
            return
        }
        if (product.product == null) {
            return
        }
        var clone = product.product!!.clone()
        if (evento.amount > PLAYER_INVENTORY_LIMIT) {
            clone = MineReflect.toStack(clone, evento.amount)
        } else {
            clone.amount = evento.amount.toInt()
        }
        if (Mine.isFull(player.inventory)) {
            val inv = Mine.newInventory("Pegue seus items comprados", 6 * 9)
            inv.addItem(clone)
            player.openInventory(inv)
            player.sendMessage("§cPegue seus items comprados e coloca no seu inventário.")

        } else {
            player.inventory.addItem(clone)
        }
    }

    fun sell(player: Player, product: Product?, value: Double) {
        var amount = value
        val fake = FakePlayer(player)
        val priceUnit = product!!.unitSellPrice
        if (amount > product.stock) {
            amount = product.stock
        }
        if (amount < 1) {
            player.sendMessage(messageWithoutItems)
            return
        }
        val finalPrice = amount * priceUnit
        val evento = ProductTradeEvent(player)
        evento.product = product
        evento.amount = amount
        evento.newStock = product.stock - amount
        evento.balance = currency!!.get(fake)
        evento.type = TradeType.SELABLE
        evento.priceTotal = finalPrice
        evento.shop = this@Shop
        //
        Mine.callEvent(evento)
        if (evento.isCancelled) {
            return
        }
        if (evento.amount > PLAYER_INVENTORY_LIMIT) {
            evento.amount = PLAYER_INVENTORY_LIMIT.toDouble()
        }
        Mine.remove(player.inventory, product.product, evento.amount.toInt())
        player.sendMessage(
            messageSoldItem.replace("\$amount", "" + amount)
                .replace(
                    "\$product", product.name
                )
        )
        currency!!.add(fake, finalPrice)
    }


    fun getProduct(icon: ItemStack, player: Player): Product? {
        val button = getButton(icon, player)
        if (button != null) {
            if (button is Product) {
                return button
            }
        }
        return null
    }

    fun getProductFrom(item: ItemStack): Product? {
        for (button in buttons)
            if (button is Product) {
                if (true == button.product?.isSimilar(item))
                    return button
            }

        return null
    }

    override fun register(plugin: IPluginInstance) {
        super.register(plugin)
        menuUpgrades?.register(plugin)
        menuConfirmation?.register(plugin)
        if (menuConfirmation != null) {
            val confirmationButton = menuConfirmation!!.getButton("confirmar")!!
            val cancelButton = menuConfirmation!!.getButton("cancelar")!!
            val productButton = menuConfirmation!!.getButton("product")!!
            cancelButton.click = ClickEffect { event -> open(event.player) }
            confirmationButton.click = ClickEffect { event ->
                val player = event.player
                val produto = selectedProduct[player]!!
                val type = trading[player]!!

                if (type == TradeType.BUYABLE) {
                    buy(player, produto, 1.0)
                } else if (type == TradeType.SELABLE) {
                    sell(player, produto, 1.0)
                }
                open(event.player)
            }
            if (menuConfirmation!!.openHandler == null)
                menuConfirmation!!.openHandler = { inventory, player ->
                    val product = selectedProduct[player]!!
                    inventory.setItem(productButton.index, product.icon)
                }
        }
        if (menuUpgrades != null) {
            menuUpgrades!!.superiorMenu = this
            if (menuUpgrades!!.openHandler == null) {
                menuUpgrades!!.openHandler = { inventory, player ->
                    val product = selectedProduct[player]!!
                    var slot = Extra.getIndex(3, 4)
                    val productButton = menuUpgrades?.getButton("product")!!
                    inventory.setItem(productButton.index, product.icon)
                    for (upgrade in product.upgrades) {
                        val icon = ItemBuilder(Material.STAINED_GLASS_PANE)
                            .data(14)
                            .name("§6Upgrade §e" + upgrade.displayName)
                        if (upgrade.hasBought(player)) {
                            icon.data(5)
                        }
                        inventory.setItem(slot, icon)
                        slot++
                    }
                }
            }
        }
        val upgradeButton = menuUpgrades?.getButton("upgrade") ?: return
        upgradeButton.click = ClickEffect { event ->
            val player = event.player
            val fake = FakePlayer(player)
            val product = selectedProduct[player]!!
            val nextUpgrade = product.getNextUpgrade(player)
            if (nextUpgrade == null) {
                player.sendMessage(messageUpgradesAlreadyBought)
                return@ClickEffect
            }
            if (!this.currency!!.contains(fake, nextUpgrade.price)) {
                player.sendMessage(messageWithoutBalance)
                return@ClickEffect
            }
            this.currency!!.remove(fake, nextUpgrade.price)
            VaultAPI.getPermission().playerAdd(player, nextUpgrade.permission)
            player.sendMessage(
                messageUpgradeBought
                    .replace(
                        "\$product_name",
                        nextUpgrade.displayName
                    )
                    .replace(
                        "\$product",
                        nextUpgrade.name
                    )
                    .replace(
                        "\$level",
                        "" + nextUpgrade.level
                    )
            )

        }
    }

    init {

        this.effect = ClickEffect { event ->
            val player = event.player
            if (event.currentItem == null) return@ClickEffect
            val product = getProduct(event.currentItem, player)
            if (product == null) {
                debug("Produto pelo Icone nulo")
                return@ClickEffect
            }

            if (menuUpgrades != null && isPermissionShop && product.hasBought(player)) {
                menuUpgrades?.open(player)
                return@ClickEffect
            } else if (menuConfirmation != null) {
                if (isPermissionShop && product.hasBought(player)) {
                    return@ClickEffect
                }
                trading[player] = product.tradeType
                selectedProduct[player] = product
                player.closeInventory()
                menuConfirmation!!.open(player)
                return@ClickEffect
            }

            if (((event.click == ClickType.RIGHT || event.click == ClickType.SHIFT_RIGHT)
                        && (product.tradeType === TradeType.BOTH))
                || (product.tradeType === TradeType.BUYABLE)
            ) {
                if (isAmountPerChat) {
                    selectingAmount[player] = product
                    trading[player] = TradeType.BUYABLE
                    player.closeInventory()
                    player.sendMessage(
                        messageChoiceAmount.replace(
                            "\$product",
                            "" + product.name
                        ).replace("\$trader", "comprar")
                    )
                    return@ClickEffect
                }
                var amount = 1
                if (event.click.name.contains("SHIFT")) {
                    amount = product.amount
                    if (amount < 64) {
                        amount = 64
                    }
                }
                buy(player, product, amount.toDouble())
            }
            if (((event.click == ClickType.LEFT || event.click == ClickType.SHIFT_LEFT)
                        && (product.tradeType === TradeType.BOTH)
                        || product.tradeType === TradeType.SELABLE)
            ) {
                if (product.product == null) return@ClickEffect
                if (isAmountPerChat) {
                    selectingAmount[player] = product
                    trading[player] = TradeType.SELABLE
                    player.closeInventory()
                    player.sendMessage(
                        messageChoiceAmount
                            .replace("\$product", product.name)
                            .replace("\$trader", "vender")
                    )
                    return@ClickEffect
                }
                var amount = Mine.getTotalAmount(player.inventory, product.product)
                if (event.click == ClickType.LEFT || event.click == ClickType.LEFT) {
                    if (amount > 64) {
                        amount = 64
                    }
                }
                sell(player, product, amount.toDouble())
            }

        }
    }


}