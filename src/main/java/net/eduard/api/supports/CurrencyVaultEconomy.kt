package net.eduard.api.supports

import net.eduard.api.lib.storage.annotations.StorageAttributes
import net.eduard.api.server.currency.SimpleCurrencySystem
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.VaultAPI
import net.eduard.api.lib.game.ItemBuilder
import org.bukkit.Material


class CurrencyVaultEconomy : SimpleCurrencySystem() {

    override operator fun get(player: FakePlayer): Double {
        return if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            VaultAPI.getEconomy().getBalance(player)
        } else (-1).toDouble()
    }



    override fun add(player: FakePlayer, amount: Double): Boolean {
        if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            VaultAPI.getEconomy().depositPlayer(player, amount)
            return true
        }
        return false
    }

    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            VaultAPI.getEconomy().has(player, amount)
        } else false
    }

    override fun remove(player: FakePlayer, amount: Double): Boolean {
        if (VaultAPI.hasVault() && VaultAPI.hasEconomy()) {
            VaultAPI.getEconomy().withdrawPlayer(player, amount)
            return true
        }
        return false
    }

    init {
        name = "VaultEconomy"
        displayName = "Dinheiro Padrão"
        icon = ItemBuilder(Material.EMERALD).name("§aDinheiro Padrão")
        symbol = "$"
    }
}