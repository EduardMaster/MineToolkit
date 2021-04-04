package net.eduard.api.server.currency

import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.server.CurrencySystem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class SimpleCurrencySystem
    (
    override var name: String = "MoedaCustom",
    override var icon: ItemStack = ItemStack(Material.DIAMOND_BLOCK),
    override var symbol: String = "\$",
    override var displayName: String = "Moeda customizada",
    override var inicialAmount: Int = 0

): CurrencySystem
{
    override var position = 1
    override fun get(player: FakePlayer): Double {
        return 0.0
    }

    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return true
    }

    override fun remove(player: FakePlayer, amount: Double): Boolean {
        return true
    }

    override fun add(player: FakePlayer, amount: Double): Boolean {

        return true
    }

    override fun set(player: FakePlayer, amount: Double){

    }

}