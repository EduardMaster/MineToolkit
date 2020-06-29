package net.eduard.api.lib.menu

import net.eduard.api.lib.game.ClickEffect
import net.eduard.api.lib.game.FakePlayer
import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.manager.CurrencyManager
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.modules.MineReflect
import net.eduard.api.lib.modules.VaultAPI
import net.eduard.api.lib.storage.Storable.StorageAttributes
import net.eduard.api.server.currency.CurrencyController
import net.eduard.api.server.currency.CurrencyHandler
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.ItemStack
import java.util.*

@StorageAttributes(indentificate = true)
open class Shop(name: String = "Loja", lineAmount: Int = 3) : Menu(name, lineAmount), ClickEffect {

    override fun onClick(event: InventoryClickEvent, page: Int) {
        if (event.whoClicked is Player) {
            val player = event.whoClicked as Player
            if (event.currentItem == null) return
            val product = getProduct(event.currentItem, player) ?: return
            if (menuConfirmation != null) {
                trading[player] = product.tradeType
                confirmingTransaction[player] = product
                menuConfirmation.open(player)
                return
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
                    return
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
                        return
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

    @Transient
    private val selectingAmount: MutableMap<Player, Product> = HashMap()

    @Transient
    var trading: MutableMap<Player, TradeType> = HashMap()

    @Transient
    val confirmingTransaction: MutableMap<Player, Product> = HashMap()
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
    var textAlreadyBought = TEXT_ALREADY_BOUGHT
    var menuConfirmation: Menu = Menu("Confirmar Transação", 3)


    companion object {
        private var MESSAGE_CHOICE_AMOUNT = "§aEscolha uma quantidade para \$trader este produto \$product"
        private var MESSAGE_BOUGHT_ITEM = "§aVoce adquiriu \$amount (\$product) produto(s) da Loja!"
        private var MESSAGE_SOLD_ITEM = "§aVocê vendeu \$amount (\$product) produtos(s) para a loja!"
        private var MESSAGE_WITHOUT_ITEMS = "§cVoce não tem items suficiente!"
        private var MESSAGE_WITHOUT_BALANCE = "§cVoce não tem dinheiro suficiente!"
        private var MESSAGE_WITHOUT_PERMISSION = "§cVoce não tem permissão para comprar este produto!"
        private var MESSAGE_ALREADY_BOUGHT = "§cVocê já comprou este produto."
        private var TEXT_ALREADY_BOUGHT = "§a§lDESBLOQUEADO"
        private var TEMPLATE_BUY = Arrays.asList("§fCompre o produto §e\$product_name",
                "§2Quantidade: §a\$product_stock", "§2Preço por 1: §a\$product_buy_unit_price", "§2Preço por 64: §a\$product_buy_pack_price")
        private var TEMPLATE_SELL = Arrays.asList("§fVende o produto: §e\$product_name",
                "§2Quantidade: §a\$product_stock", "§2Preço por 64: §a\$product_sell_pack_price", "§2Preço por Inventario: §a\$product_sell_inventory_price")
        private var TEMPLATE_BUY_SELL = Arrays.asList("§fCompra e venda de: §e\$product_name",
                "§2Quantidade: §a\$product_stock", "§2Preço por 64: §a\$product_sell_pack_price", "§2Preço por Inventario: §a\$product_sell_inventory_price", "", "§2Preço por 1: §a\$product_buy_unit_price", "§2Preço por 64: §a\$product_buy_pack_price")
        private const val PLAYER_INVENTORY_LIMIT = 4 * 64 * 9
    }
    init{
        useConfirmationMenu()
    }

    fun useConfirmationMenu() {
        menuConfirmation.superiorMenu = this
        menuConfirmation.register(pluginInstance)
        val confirmationButton = MenuButton("confirmar", menuConfirmation)
        confirmationButton.setPosition(3, 2)
        confirmationButton.icon = ItemBuilder(Material.WOOL).data(5).name("§a§lCONFIRMAR").lore("§aClique para confirmar a transação.")
        val cancelButton = MenuButton("cancelar", menuConfirmation)
        cancelButton.icon = ItemBuilder(Material.WOOL).data(14).name("§c§lCANCELAR").lore("§cClique para cancelar a transação.")
        cancelButton.setPosition(7, 2)
        cancelButton.click = ClickEffect { event: InventoryClickEvent, page: Int -> open((event.whoClicked as Player), page) }
        val productButton = MenuButton("product", menuConfirmation)
        productButton.setPosition(5, 2)
        productButton.icon = ItemBuilder(Material.STONE)
        confirmationButton.click = ClickEffect { event: InventoryClickEvent, page: Int ->
            if (event.whoClicked is Player) {
                val player = event.whoClicked as Player
                val produto = confirmingTransaction[player]!!
                val type = trading[player]!!
                if (type == TradeType.BUYABLE) {
                    buy(player, produto, 1.0)
                } else if (type == TradeType.SELABLE) {
                    sell(player, produto, 1.0)
                }
            }
        }
    }

    @EventHandler
    fun openInventory(e: InventoryOpenEvent) {
        if (e.player is Player) {
            val player = e.player as Player

            if (menuConfirmation.isOpen(player)) {
                val productButton = menuConfirmation.getButton("product")!!
                val product = confirmingTransaction[player]!!
                e.inventory.setItem(productButton.index, product.icon)
            }

        }
    }

    override fun copy(): Shop {
        return copy(this)
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
                "" + product.name))
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
                    return button as Product
                }
            }
        }
        return null
    }


    init {
        this.effect = this
    }

}