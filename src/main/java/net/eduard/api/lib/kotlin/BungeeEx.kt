package net.eduard.api.lib.kotlin

import net.eduard.api.lib.hybrid.PlayerUser
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import org.bukkit.entity.Player
import java.util.UUID


val String.bungeePlayer get() = ProxyServer.getInstance().getPlayer(this)

val UUID.bungeePlayer get() = ProxyServer.getInstance().getPlayer(this)

fun Listener.register(plugin : Plugin){
    ProxyServer.getInstance().pluginManager.registerListener(plugin,this)
}
val ProxiedPlayer.offline get() = PlayerUser(this.name,this.uniqueId)



fun ProxiedPlayer.sendConfirmationQuery(acceptCommand: String, declineCommand: String) {

    sendMessage(confirmationChat(acceptCommand, declineCommand))
}


