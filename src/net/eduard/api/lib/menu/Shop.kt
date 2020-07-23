package net.eduard.api.lib.menu

import net.eduard.api.lib.game.ClickEffect
import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.kotlin.player
import net.eduard.api.lib.manager.CurrencyManager
import net.eduard.api.lib.modules.*
import net.eduard.api.lib.plugin.IPluginInstance
import net.eduard.api.lib.storage.Storable.StorageAttributes
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.CurrencyHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

@StorageAttributes(indentificate = true)
open class Shop(name: String = "Loja", lineAmount: Int = 3
                , block: (Shop.() -> Unit)? = null) : Menu(name, lineAmount) {
    init {
        block?.invoke(this)
    }

    fun product(name: String = "Produto", block: (Product.() -> Unit)? = null): Product {
        val product = Product(name, this)
        block?.invoke(product)
        return product
    }


    var currencyType = "VaultEconomy"

    @Transient
    var currency: CurrencyHandler? = null
        get() {

            if (field == null) {
                field = CurrencyController.getInstance().getCurrencyHandler(currencyType)
            }
            return field

        }
    var sortType = ShopSortType.BUY_PRICE_ASC
    var menuUpgrades: Menu? = null

    @Transient
    var selectingAmount: MutableMap<Player, Product> = HashMap()

    @Transient
    var trading: MutableMap<Player, TradeType> = HashMap()

    @Transient
    val selectedProduct: MutableMap<Player, Product> = HashMap()
    var isAmountPerChat = false
    var isPermissionShop = false
    var buyTemplate: MutableList<String> = ArrayList(TEMPLATE_BUY)
    var sellTemplate: MutableList<String> = ArrayList(TEMPLATE_SELL)
    var sellBuyTemplate: MutableList<String> = ArrayList(TEMPLATE_BUY_SELL)
    var messageChoiceAmount = MESSAGE_CHOICE_AMOUNT
    var messageBoughtItem = MESSAGE_BOUGHT_ITEM
    var messageSoldItem = MESSAGE_SOLD_ITEM
    var messageAlreadyBought = MESSAGE_ALREADY_BOUGHT
    var messageWithoutItems = MESSAGE_WITHOUT_ITEMS
    var messageWithoutBalance = MESSAGE_WITHOUT_BALANCE
    var messageWithoutPermission = MESSAGE_WITHOUT_PERMISSION
    var messageUpgradeBought = MESSAGE_UPGRADE_BOUGHT
    var messageUpgradesAlreadyBought = MESSAGE_UPGRADES_ALREADY_BOUGHT
    var textAlreadyBought = TEXT_ALREADY_BOUGHT

    var menuConfirmation: Menu? = null


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
        var TEXT_ALREADY_BOUGHT = "§a§lDESBLOQUEADO"

        var TEMPLATE_BUY = listOf("§fCompre o produto §e\$product_name",
                "§2Quantidade: §a\$product_stock", "§2Preço por 1: §a\$product_buy_unit_price", "§2Preço por 64: §a\$product_buy_pack_price")
        var TEMPLATE_SELL = listOf("§fVende o produto: §e\$product_name",
                "§2Quantidade: §a\$product_stock", "§2Preço por 64: §a\$product_sell_pack_price", "§2Preço por Inventario: §a\$product_sell_inventory_price")
        var TEMPLATE_BUY_SELL = listOf("§fCompra e venda de: §e\$product_name",
                "§2Quantidade: §a\$product_stock", "§2Preço por 64: §a\$product_sell_pack_price", "§2Preço por Inventario: §a\$product_sell_inventory_price", "", "§2Preço por 1: §a\$product_buy_unit_price", "§2Preço por 64: §a\$product_buy_pack_price")
        const val PLAYER_INVENTORY_LIMIT = 4 * 64 * 9
    }


    fun useUpgradesMenu() {
        menuUpgrades = Menu("Lista de Upgrades", 6) {
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
        menuConfirmation = Menu("Confirmar Transação", 3) {


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

    @EventHandler
    fun openInventory(e: InventoryOpenEvent) {
        if (e.player is Player) {
            val player = e.player as Player

            if (menuConfirmation?.isOpen(player) == true) {

                val productButton = menuConfirmation?.getButton("product")!!

                val product = selectedProduct[player]!!
                e.inventory.setItem(productButton.index, product.icon)
                if (isPermissionShop) {
                    if (product.hasBought(player)) {
                        e.isCancelled = true
                        menuUpgrades?.open(player)
                    }
                }

            } else {
                menuUpgrades ?: return
                if (isPermissionShop && menuUpgrades!!.isOpen(player)) {
                    val product = selectedProduct[player]!!
                    var slot = Extra.getIndex(2, 3)
                    for (upgrade in product.upgrades) {
                        var icon = ItemBuilder(Material.STAINED_GLASS_PANE).data(14)
                                .name(upgrade.displayName)
                        if (upgrade.hasBought(player)) {
                            icon.data(4)
                        }
                        e.inventory.setItem(slot, icon)
                        slot++


                    }

                }
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
        val p = e.player
        if (selectingAmount.containsKey(p)) {
            val product = selectingAmount[p]!!
            var amount = Extra.fromMoneyToDouble(e.message)
            amount = Math.abs(amount)
            selectingAmount.remove(p)
            val trade = trading[p]
            trading.remove(p)
            if (trade === TradeType.BUYABLE) {
                buy(p, product, amount)
            } else if (trade === TradeType.SELABLE) {
                sell(p, product, amount)
            }
            e.isCancelled = true
        }
    }

    fun buy(player: Player, product: Product, amount: Double) {
        var amount = amount
        val fake = FakePlayer(player)
        val priceUnit = product!!.unitBuyPrice
        if (product.isLimited && amount > product.stock) {
            amount = product.stock
        }
        if (isPermissionShop) {
            if (player.hasPermission(product.permission)) {
                player.sendMessage(messageAlreadyBought)
                return;
            }
        } else {
            if (!player.hasPermission(product.permission)) {
                player.sendMessage(messageWithoutPermission)
                return;
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
        evento.balance = currency!![fake]
        evento.type = TradeType.BUYABLE
        evento.priceTotal = priceFinal
        evento.shop = this@Shop
        Mine.callEvent(evento)
        if (evento.isCancelled) {
            return
        }
        if (currency!![fake] >= evento.priceTotal) {
            currency!!.remove(fake, evento.priceTotal)
        } else {
            player.sendMessage(messageWithoutBalance)
            return
        }
        product.stock = evento.newStock
        for (cmd in product.commands) {
            Mine.runCommand(cmd.replace("\$player", player.name).replace("\$formated_amount", Extra.formatMoney(amount)).replace("\$amount", "" + amount))
        }
        player.sendMessage(messageBoughtItem.replace("\$amount", Extra.formatMoney(amount)).replace("\$product",
                "" + product.name))
        if (isPermissionShop) {
            if (VaultAPI.hasVault() && VaultAPI.hasPermission()) {
                VaultAPI.getPermission().playerAdd(null, fake, product.permission)
            }
            return;
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

    fun sell(player: Player, product: Product?, amount: Double) {
        var amount = amount
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
        evento.balance = currency!![fake]
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
        player.sendMessage(messageSoldItem.replace("\$amount", "" + amount).replace("\$product",
                product.name))
        currency!!.add(fake, finalPrice)
    }


    fun setCurrency(currency: CurrencyManager?) {
        if (currency == null) return
        this.currency = currency
        for (button in buttons) {
            if (button.menu is Shop) {
                val shop = button.shop
                shop.setCurrency(currency)
            }
        }
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
        for (button in buttons) {
            if (button is Product) {
                if (button.product?.isSimilar(item)!!) {
                    return button
                }
            }
        }
        return null
    }

    override fun register(plugin: IPluginInstance) {
        super.register(plugin)
        menuUpgrades?.superiorMenu = this
        menuConfirmation?.superiorMenu = this
        if (menuConfirmation!= null) {
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

            }
        }
        val upgradeButton = menuUpgrades?.getButton("upgrade") ?: return
        upgradeButton.click = ClickEffect { event ->
            val p = event.player
            val fake = FakePlayer(p)
            val product = selectedProduct[p]!!
            val nextUpgrade = product.getNextUpgrade(p)
            if (nextUpgrade != null) {
                if (this.currency!!.contains(fake, nextUpgrade.price)) {

                    this.currency!!.remove(fake, nextUpgrade.price)
                    VaultAPI.getPermission().playerAdd(p, nextUpgrade.permission)
                    p.sendMessage(messageUpgradeBought
                            .replace("\$product_name", nextUpgrade.displayName)
                            .replace("\$product", nextUpgrade.name)

                            .replace("\$level", "" + nextUpgrade.level))
                } else {
                    p.sendMessage(messageWithoutBalance)
                }
            } else {
                p.sendMessage(messageUpgradesAlreadyBought)
            }
        }

    }

    init {
        useConfirmationMenu()
        this.effect = ClickEffect { event ->


            val player = event.player
            if (event.currentItem == null) return@ClickEffect
            val product = getProduct(event.currentItem, player) ?: return@ClickEffect
            if (menuConfirmation != null) {
                trading[player] = product.tradeType
                selectedProduct[player] = product

                menuConfirmation?.open(player)
                return@ClickEffect
            }
            if ((event.click == ClickType.RIGHT || event.click == ClickType.SHIFT_RIGHT)
                    && (product.tradeType === TradeType.BOTH
                            || product.tradeType === TradeType.BUYABLE)) {
                if (isAmountPerChat) {
                    selectingAmount[player] = product
                    trading[player] = TradeType.BUYABLE
                    player.closeInventory()
                    player.sendMessage(messageChoiceAmount.replace("\$product",
                            "" + product.name).replace("\$trader", "comprar"))
                    return@ClickEffect
                }
                var amount = 1
                if (event.click == ClickType.SHIFT_RIGHT) {
                    amount = product.amount
                    if (amount < 64) {
                        amount = 64
                    }
                }
                buy(player, product, amount.toDouble())
            }
            if ((event.click == ClickType.LEFT || event.click == ClickType.SHIFT_LEFT)
                    && (product.tradeType === TradeType.BOTH
                            || product.tradeType === TradeType.SELABLE)) {
                if (product.product != null) {
                    if (isAmountPerChat) {
                        selectingAmount[player] = product
                        trading[player] = TradeType.SELABLE
                        player.closeInventory()
                        player.sendMessage(messageChoiceAmount.replace("\$product",
                                "" + product.name).replace("\$trader", "vender"))
                        return@ClickEffect
                    }
                    var amount = Mine.getTotalAmount(player.inventory, product.product)
                    if (event.click == ClickType.LEFT) {
                        if (amount > 64) {
                            amount = 64
                        }
                    }
                    sell(player, product, amount.toDouble())
                }
            }
        }
    }


}