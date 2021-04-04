package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer
import org.bukkit.inventory.ItemStack


interface CurrencySystem {

    var name: String
    var displayName: String
    var icon: ItemStack
    var symbol: String
    var inicialAmount: Int
    var position: Int
    fun get(player: FakePlayer): Double
    fun contains(player: FakePlayer, amount: Double): Boolean
    fun remove(player: FakePlayer, amount: Double): Boolean
    fun add(player: FakePlayer, amount: Double): Boolean
    fun set(player: FakePlayer, amount: Double)


}