package net.eduard.api.lib.command

import net.md_5.bungee.api.chat.TextComponent


abstract class PlayerSender<PlayerClass>(name: String) : Sender(name) {


    abstract val player: PlayerClass

    abstract fun sendMessage(str: TextComponent)
    abstract fun connect(serverName: String)

    abstract var fly: Boolean


}