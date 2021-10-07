package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer


interface SoulSystem {

    fun addSouls(player: FakePlayer, amount: Double)
    fun removeSouls(player: FakePlayer, amount: Double)
    fun getSouls(player: FakePlayer): Double
    fun setSouls(player: FakePlayer, amount: Double)

}
