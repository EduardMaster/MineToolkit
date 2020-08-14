package net.eduard.api.lib.kotlin

import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import java.util.UUID


val String.bungeePlayer get() = BungeeCord.getInstance().getPlayer(this)

val UUID.bungeePlayer get() = BungeeCord.getInstance().getPlayer(this)

inline fun Listener.register(plugin : Plugin){
    BungeeCord.getInstance().pluginManager.registerListener(plugin,this)
}



fun ProxiedPlayer.sendConfirmationQuery(acceptCommand: String, declineCommand: String) {

    sendMessage(confirmationChat(acceptCommand, declineCommand))
}


