package net.eduard.api.lib.hybrid

import java.util.*

interface IServer {


    fun getPlayer(name : String, uuid : UUID) : IPlayer<*>
    fun getPlayer(name : String) : IPlayer<*>
    fun getPlayer(uuid : UUID) : IPlayer<*>
    val console : ISender
    val isBungeecord : Boolean
}