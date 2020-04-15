package net.eduard.api.server

import net.eduard.api.lib.player.FakePlayer

interface RankSystem {

    val ranksNames: List<String>

    fun getRank(player: FakePlayer): String

    fun getRankPrefix(player: FakePlayer): String

    fun getRankSuffix(player: FakePlayer): String

    fun getRankProgress(player: FakePlayer): Double


}
