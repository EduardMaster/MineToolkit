package net.eduard.api.server

import net.eduard.api.lib.game.FakePlayer

interface PartySystem {

    fun isInParty(player: FakePlayer): Boolean

    fun isLeader(player: FakePlayer): Boolean

    fun isMember(player: FakePlayer): Boolean

    fun getPartyLeader(player: FakePlayer): FakePlayer

    fun getPartyMembers(player: FakePlayer): List<FakePlayer>

}
