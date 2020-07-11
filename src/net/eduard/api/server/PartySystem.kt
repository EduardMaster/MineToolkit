package net.eduard.api.server

import net.eduard.api.lib.command.PlayerOffline

interface PartySystem {

    fun isInParty(player: PlayerOffline): Boolean

    fun isLeader(player: PlayerOffline): Boolean

    fun isMember(player: PlayerOffline): Boolean

    fun getPartyLeader(player: PlayerOffline): PlayerOffline

    fun getPartyMembers(player: PlayerOffline): List<PlayerOffline>

}
