package net.eduard.api.server

import net.eduard.api.lib.game.FakePlayer

interface CashSystem {

    fun addCash(player: FakePlayer, amount: Double)

    fun removeCash(player: FakePlayer, amount: Double)

    fun getCash(player: FakePlayer): Double

    fun setCash(player: FakePlayer, amount: Double)

    fun hasCash(player: FakePlayer, amount: Double): Boolean {
        return getCash(player) >= amount
    }

}
