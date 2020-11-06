package net.eduard.api.server

import lib.modules.FakePlayer


interface SoulSystem {

    fun addSouls(player: lib.modules.FakePlayer, amount: Double)

    fun removeSouls(player: lib.modules.FakePlayer, amount: Double)

    fun getSouls(player: lib.modules.FakePlayer): Double

    fun setSouls(player: lib.modules.FakePlayer, amount: Double)

}
