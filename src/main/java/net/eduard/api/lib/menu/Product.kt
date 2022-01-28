package net.eduard.api.lib.menu

import net.eduard.api.lib.game.EnchantGlow
import net.eduard.api.lib.kotlin.format
import net.eduard.api.lib.modules.Mine
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("unused")
open class Product(
    name: String, menu: Menu?
) : MenuButton(name, menu,1,1,1) {


    constructor() : this("Produto", null)

    fun upgrade(level: Int, body: ProductUpgrade.() -> Unit): ProductUpgrade {
        val upgrade = ProductUpgrade(level, this)
        body.invoke(upgrade)
        return upgrade
    }

    var display = name
    var sellPrice = 0.0
    var buyPrice = 0.0
    var isLimited = false
    var stock = 0.0
    var tradeType = TradeType.BUYABLE
    var permission = "produto.permissao"
    var commands = mutableListOf<String>()
    var upgrades = mutableListOf<ProductUpgrade>()
    val hasUpgrades get() = upgrades.isNotEmpty()

    @Transient
    lateinit var realProduct: Any

    fun hasRealProductLinked() = this::realProduct.isInitialized

    var product: ItemStack? = null
        set(value) {
            field = value
            if (item == null) {
                item = value?.clone()
            }
        }
        get() {
            if (field != null) {
                if (item == null) {
                    item = field
                }
            }
            return field
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

    var moneyFormatedOP = false

    override fun getIcon(player: Player): ItemStack {
        val iconClone = super.getIcon(player).clone()
        if (iconClone.amount > 64) {
            iconClone.amount = 64
        }
        val lore = Mine.getLore(iconClone)
        if (parentMenu == null) {
            return iconClone
        }
        if (parentMenu !is Shop) {
            return iconClone
        }
        var template: List<String>? = null
        if (tradeType === TradeType.BUYABLE) {
            template = parentShop.buyTemplate
            if (parentShop.isPermissionShop) {
                if (player.hasPermission(permission)) {
                    if (parentShop.glowIconWhenAlreadyBought) {
                        EnchantGlow.addGlow(iconClone)
                    }
                    template = parentShop.boughtTemplate
                }
            }
        } else if (tradeType === TradeType.SELABLE) {
            template = parentShop.sellTemplate
        } else if (tradeType === TradeType.BOTH) {
            template = parentShop.sellBuyTemplate
        }
        val productIconAmount = iconClone.amount
        for (line in template!!) {
            lore.add(
            line.replace("%product_name", display,false)
                .replace("%product_stock",  stock.format(moneyFormatedOP),false)
                .replace("%product_buy_unit_price", unitBuyPrice.format(moneyFormatedOP),false)
                .replace("%product_buy_pack_price", (unitBuyPrice * 64.0).format(moneyFormatedOP),false)
                .replace("%product_buy_x_price", (unitBuyPrice * productIconAmount).format(moneyFormatedOP),false)
                .replace("%product_amount" , ""+ productIconAmount)
                .replace("%product_sell_unit_price", unitSellPrice.format(moneyFormatedOP),false)
                .replace("%product_sell_pack_price", (unitSellPrice * 64).format(moneyFormatedOP),false)
                .replace("%product_buy_total_price", (unitBuyPrice * stock).format(moneyFormatedOP),false)
                .replace("%product_sell_total_price", (unitSellPrice * stock).format(moneyFormatedOP),false)
                .replace("%product_sell_inventory_price", (unitSellPrice * 64 * 4 * 9).format(moneyFormatedOP))
            )
        }

        Mine.setLore(iconClone, lore)
        return iconClone
    }

}