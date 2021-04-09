package net.eduard.api.lib.menu

import net.eduard.api.lib.game.EnchantGlow
import net.eduard.api.lib.kotlin.format
import net.eduard.api.lib.modules.Mine
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("unused")
open class Product(
    name: String = "Produto", shop: Shop? = null
) : MenuButton(name, shop) {


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
    var permission = "produto.permissao"
    var commands = mutableListOf<String>()
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
        product
        var clone = super.getIcon(player).clone()
        clone = clone.clone()
        if (isLimited) {
            if (clone.amount > 64) {
                clone.amount = 64
            }
        }
        val lore = Mine.getLore(clone)
        if (parentMenu == null) {
            return clone
        }
        if (parentMenu !is Shop) {
            return clone
        }
        var template: List<String>? = null
        if (tradeType === TradeType.BUYABLE) {
            template = parentShop.buyTemplate
            if (parentShop.isPermissionShop) {
                if (player.hasPermission(permission)) {
                    if (parentShop.glowIconWhenAlreadyBought) {
                        EnchantGlow.addGlow(clone)
                    }
                    template = parentShop.boughtTemplate
                }
            }
        } else if (tradeType === TradeType.SELABLE) {
            template = parentShop.sellTemplate
        } else if (tradeType === TradeType.BOTH) {
            template = parentShop.sellBuyTemplate
        }
        for (line in template!!) {
            lore.add(
            line.replace("\$product_name", name)
                .replace("\$product_stock", "" + stock)
                .replace("\$product_buy_unit_price", unitBuyPrice.format(moneyFormatedOP))
                .replace("\$product_buy_pack_price", (unitBuyPrice * 64.0).format(moneyFormatedOP))
                .replace("\$product_sell_unit_price", unitSellPrice.format(moneyFormatedOP))
                .replace("\$product_sell_pack_price", (unitSellPrice * 64).format(moneyFormatedOP))
                .replace(
                    "\$product_sell_inventory_price",
                    (unitSellPrice * 64 * 4 * 9).format(moneyFormatedOP)
                )
            )
        }

        Mine.setLore(clone, lore)
        return clone
    }

}