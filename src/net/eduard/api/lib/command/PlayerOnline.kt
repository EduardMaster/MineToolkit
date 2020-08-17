package net.eduard.api.lib.command

import net.md_5.bungee.api.chat.TextComponent
import java.util.*


abstract class PlayerOnline<PlayerClass>(name: String, uuid : UUID) : Sender(name,uuid) {


    abstract val player: PlayerClass

    abstract fun sendMessage(str: TextComponent)
    abstract fun connect(serverName: String)

    abstract var fly: Boolean

    abstract val isOffline : Boolean

    val offline = PlayerOffline(name,uuid)


}