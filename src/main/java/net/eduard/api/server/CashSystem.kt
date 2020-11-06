package net.eduard.api.server

import lib.modules.FakePlayer


interface CashSystem {

    fun addCash(player: lib.modules.FakePlayer, amount: Double)

    fun removeCash(player: lib.modules.FakePlayer, amount: Double)

    fun getCash(player: lib.modules.FakePlayer): Double

    fun setCash(player: lib.modules.FakePlayer, amount: Double)

    fun hasCash(player: lib.modules.FakePlayer, amount: Double): Boolean {
        return getCash(player) >= amount
    }

}
