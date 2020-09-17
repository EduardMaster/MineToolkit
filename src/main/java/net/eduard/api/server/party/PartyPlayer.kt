package net.eduard.api.server.party

import net.eduard.api.lib.command.PlayerOffline

interface PartyPlayer {

    val offline : PlayerOffline
    var party : Party?
    fun hasParty() : Boolean
    fun isLeader() : Boolean

}