package net.eduard.api.server


interface CashSystem {

    fun addCash(player: net.eduard.api.lib.modules.FakePlayer, amount: Double)

    fun removeCash(player: net.eduard.api.lib.modules.FakePlayer, amount: Double)

    fun getCash(player: net.eduard.api.lib.modules.FakePlayer): Double

    fun setCash(player: net.eduard.api.lib.modules.FakePlayer, amount: Double)

    fun hasCash(player: net.eduard.api.lib.modules.FakePlayer, amount: Double): Boolean {
        return getCash(player) >= amount
    }

}
