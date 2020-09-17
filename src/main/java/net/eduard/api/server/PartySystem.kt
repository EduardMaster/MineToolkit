package net.eduard.api.server

import net.eduard.api.lib.command.PlayerOffline
import net.eduard.api.server.party.Party
import net.eduard.api.server.party.PartyPlayer

interface PartySystem {

    fun isInParty(player: PlayerOffline): Boolean

    fun isLeader(player: PlayerOffline): Boolean

    fun isMember(player: PlayerOffline): Boolean

    fun getPartyLeader(player: PlayerOffline): PlayerOffline

    fun getPartyMembers(player: PlayerOffline): List<PlayerOffline>

    fun getParty(player : PlayerOffline) : Party

    fun getPlayer(player : PlayerOffline) : PartyPlayer
}
