package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer

interface SoulSystem {

    fun addSouls(player: net.eduard.api.lib.modules.FakePlayer, amount: Double)

    fun removeSouls(player: net.eduard.api.lib.modules.FakePlayer, amount: Double)

    fun getSouls(player: net.eduard.api.lib.modules.FakePlayer): Double

    fun setSouls(player: net.eduard.api.lib.modules.FakePlayer, amount: Double)

}
