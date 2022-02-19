package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer

/**
 * API de Verificar Tempo Jogando (Tempo de Jogo)
 */
interface PlaytimeSystem {
    fun getPlaytime(player : FakePlayer) : Long
    fun getStaffPlaytime(player : FakePlayer) : Long
    fun getStaffDayPlaytime(player : FakePlayer) : Long
    fun getStaffWeekPlaytime(player : FakePlayer) : Long
}