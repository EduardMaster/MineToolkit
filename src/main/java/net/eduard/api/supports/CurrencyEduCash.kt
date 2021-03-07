package net.eduard.api.supports

import net.eduard.api.lib.storage.annotations.StorageAttributes
import net.eduard.api.server.currency.SimpleCurrencySystem
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.server.Systems

@StorageAttributes(indentificate = true)
class CurrencyEduCash : SimpleCurrencySystem() {
    override operator fun get(player: FakePlayer): Double {
        return Systems.cashSystem!!.getCash(player)
    }

    override fun contains(player: FakePlayer, amount: Double): Boolean {
        return Systems.cashSystem!!.hasCash(player, amount)
    }

    override fun remove(player: FakePlayer, amount: Double): Boolean {
        Systems.cashSystem!!.removeCash(player, amount)
        return true
    }


    override fun add(player: FakePlayer, amount: Double): Boolean {
        Systems.cashSystem!!.addCash(player, amount)
        return true
    }

    init {
        name = "EduCash"
        displayName = "Sistema de Cash"
    }
}