package net.eduard.api.lib.menu

import lib.game.EnchantGlow
import lib.modules.Extra
import lib.modules.Mine
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

open class Product(name: String = "Produto",
                   shop: Shop? = null)
    : MenuButton(name, parentMenu = shop) {

    constructor(shopping: Shop) : this(shop = shopping)

    constructor(shopping: Shop, product: ItemStack, buyPrice: Double) : this(shop = shopping) {
        this.product = product
        this.buyPrice = buyPrice
    }

    fun upgrade(level: Int, body: ProductUpgrade.() -> Unit): ProductUpgrade {
        val upgrade = ProductUpgrade(level, this)
        body.invoke(upgrade)
        return upgrade
    }


    var sellPrice = 0.0
    var buyPrice = 0.0
    var isLimited = false
    var stock = 0.0
    var tradeType = TradeType.BUYABLE
    var permission: String = "produto.permissao"
    var commands: MutableList<String> = ArrayList()
    var upgrades = mutableListOf<ProductUpgrade>()
    val hasUpgrades get() = upgrades.isNotEmpty()
    @Transient
    lateinit var realProduct: Any
    var product: ItemStack? = null
        set(value) {
            field = value
            if (item == null) {
                item = value?.clone()
            }
        }
    val parentShop: Shop
        get() = parentMenu as Shop

    val amount: Int
        get() = if (product != null) product!!.amount else 1

    val unitSellPrice: Double
        get() = sellPrice / amount

    val unitBuyPrice: Double
        get() = buyPrice / amount

    fun hasBought(player: Player) = player.hasPermission(permission)

    fun hasBoughtAllUpgrades(player: Player): Boolean {
        if (!hasBought(player)) return false
        for (upgrade in upgrades) {
            if (!upgrade.hasBought(player)) return false
        }
        return true
    }

    fun getNextUpgrade(player: Player): ProductUpgrade? {
        for (upgrade in upgrades) {
            if (!upgrade.hasBought(player)) return upgrade
        }
        return null
    }




    override fun getIcon(player: Player): ItemStack {

        var clone = super.getIcon(player).clone()
        clone = clone.clone()
        if (isLimited) {
            if (clone.amount > 64){
                clone.amount = 64
            }
        }
        val lore = lib.modules.Mine.getLore(clone)
        if (parentMenu != null) {
            if (parentMenu is Shop) {
                var template: List<String>? = null
                if (tradeType === TradeType.BUYABLE) {
                    template = parentShop.buyTemplate
                }
                if (tradeType === TradeType.SELABLE) {
                    template = parentShop.sellTemplate
                }
                if (tradeType === TradeType.BOTH) {
                    template = parentShop.sellBuyTemplate
                }
                for (line in template!!) {
                    lore.add(line.replace("\$product_name", name)
                            .replace("\$product_stock", "" + stock)
                            .replace("\$product_buy_unit_price", lib.modules.Extra.formatMoney(unitBuyPrice))
                            .replace("\$product_buy_pack_price", lib.modules.Extra.formatMoney(unitBuyPrice * 64))
                            .replace("\$product_sell_unit_price", lib.modules.Extra.formatMoney(unitSellPrice))
                            .replace("\$product_sell_pack_price", lib.modules.Extra.formatMoney(unitSellPrice * 64))
                            .replace("\$product_sell_inventory_price", lib.modules.Extra.formatMoney(unitSellPrice * 64 * 4 * 9)))
                }
                if (parentShop.isPermissionShop) {
                    if (player.hasPermission(permission)) {
                        lib.game.EnchantGlow.addGlow(clone)
                        lore.add(parentShop.textAlreadyBought)
                    }
                }
            }
        }


        lib.modules.Mine.setLore(clone, lore)
        return clone
    }

}