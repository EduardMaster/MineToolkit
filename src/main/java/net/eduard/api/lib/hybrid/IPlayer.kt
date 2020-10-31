package net.eduard.api.lib.hybrid

import java.util.*

interface IPlayer<T : Any> : ISender {
    val uuid : UUID
    val instance : T?
    val isOnline : Boolean
    val uniqueId get() = uuid
    fun search() : T?
    fun connect(serverName: String)

}