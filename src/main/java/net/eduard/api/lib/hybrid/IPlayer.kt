package net.eduard.api.lib.hybrid

import net.md_5.bungee.api.chat.TextComponent
import java.util.*

interface IPlayer<T : Any> : ISender {
    var offline : PlayerUser
    val uniquedId : UUID
    val instance : T?
    val isOnline : Boolean
    val server : String
    fun search() : T?
    fun connect(serverName: String)
    fun playSound(soundName : String)
    fun sendMessage(message : TextComponent)
    fun chat(message : String)


}