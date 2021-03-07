package net.eduard.api.server.currency

import net.eduard.api.server.CurrencySystem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

abstract class SimpleCurrencySystem
    (
    override var name: String = "MoedaCustom",
    override var icon: ItemStack = ItemStack(Material.DIAMOND_BLOCK),
    override var symbol: String = "\$",
    override var displayName: String = "Moeda customizada",
    override var inicialAmount: Int = 0

): CurrencySystem
{
    override var position = 1

}