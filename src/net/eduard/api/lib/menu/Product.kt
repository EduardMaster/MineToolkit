package net.eduard.api.lib.menu

import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.modules.Mine
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*

open class Product(name : String = "Produto",
shop : Shop? = null)
    : MenuButton(name,parentMenu = shop) {

    var sellPrice = 0.0
    var buyPrice = 0.0
    var isLimited = false
    var stock = 0.0
    var tradeType = TradeType.BUYABLE
    var permission: String = "produto.permissao"
    var commands: List<String> = ArrayList()
    var product: ItemStack? = null
    val parentShop: Shop
        get() = parentMenu as Shop

    val amount: Int
        get() = if (product != null) product!!.amount else 1

    val unitSellPrice: Double
        get() = sellPrice / amount

    val unitBuyPrice: Double
        get() = buyPrice / amount

    override var item: ItemStack?
        get() {
            var clone = super.item
            if (clone == null) {
                clone = product
            }
            clone = clone!!.clone()
            if (isLimited) {
                clone.amount = 64
            }
            val lore = Mine.getLore(clone)
            if (name == null) {
                name = "Produto"
            }
            val parentShop: Shop? = parentShop
            if (parentShop != null) {
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
                    lore.add(line.replace("\$product_name", name).replace("\$product_stock", "" + stock).replace("\$product_buy_unit_price", Extra.formatMoney(unitBuyPrice)).replace("\$product_buy_pack_price", Extra.formatMoney(unitBuyPrice * 64)).replace("\$product_sell_unit_price", Extra.formatMoney(unitSellPrice)).replace("\$product_sell_pack_price", Extra.formatMoney(unitSellPrice * 64)).replace("\$product_sell_inventory_price", Extra.formatMoney(unitSellPrice * 64 * 4 * 9)))
                }
            }
            Mine.setLore(clone, lore)
            return clone
        }
        set(item) {
            product = item
        }

}