package net.eduard.api.supports

import JH_Shop.Main
import net.eduard.api.server.currency.SimpleCurrencySystem
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.game.ItemBuilder
import net.eduard.api.lib.storage.annotations.StorageAttributes
import org.bukkit.Material

@StorageAttributes(indentificate = true)
class CurrencyJHCash : SimpleCurrencySystem() {
    override operator fun get(player: FakePlayer): Double {
        return Main.getAPI().getPontos(player.name)
    }

    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return Main.getAPI().getPontos(player.name) >= amount
    }

    override fun remove(player: FakePlayer, amount: Double): Boolean {
        Main.getAPI().removePontos(player.name, amount)
        return true
    }



    override fun add(player: FakePlayer, amount: Double): Boolean {
        Main.getAPI().addPontos(player.name, amount)
        return true
    }

    init {
        name = "JHCash"
        displayName = "Sistema de Cash"
        symbol = "$"
        icon = ItemBuilder(Material.DIAMOND_BLOCK).name("§aCash")
    }
}