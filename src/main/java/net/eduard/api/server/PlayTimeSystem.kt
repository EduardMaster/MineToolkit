package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer

/**
 * API de Verificar Tempo Jogando (Tempo de Jogo)
 */
interface PlayTimeSystem {
    fun getPlayTime(player : FakePlayer) : Long
    fun getStaffPlayTime(player : FakePlayer) : Long
    fun getStaffDayPlayTime(player : FakePlayer) : Long
    fun getStaffWeekPlayTime(player : FakePlayer) : Long
}