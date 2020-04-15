package net.eduard.api.server

import net.eduard.api.server.currency.CashSystem
import net.eduard.api.server.currency.SoulSystem


object Systems {
    @JvmStatic
    var rankSystem: RankSystem? = null
    @JvmStatic
    var cashSystem: CashSystem? = null

    @JvmStatic
    var soulSystem: SoulSystem? = null

    @JvmStatic
    var scoreSystem: ScoreSystem? = null
    @JvmStatic
    var partySystem: PartySystem? = null
    @JvmStatic
    var generatorSystem: GeneratorSystem? = null
}
