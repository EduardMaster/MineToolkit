package net.eduard.api.lib.kotlin

import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.UUID


val String.bungeePlayer get() = BungeeCord.getInstance().getPlayer(this)

val UUID.bungeePlayer get() = BungeeCord.getInstance().getPlayer(this)



fun ProxiedPlayer.sendConfirmationQuery(acceptCommand: String, declineCommand: String) {

    sendMessage(confirmationChat(acceptCommand, declineCommand))
}


