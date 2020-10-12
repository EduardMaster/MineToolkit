package net.eduard.api.lib.command

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import java.util.*

object ConsoleSender : Sender("CONSOLE", UUID.nameUUIDFromBytes("CONSOLE".toByteArray())) {
    override fun sendMessage(str: String) {
        try {
            Bukkit.getConsoleSender().sendMessage(str)
        } catch (er: Error) {
            ProxyServer.getInstance().console.sendMessage(TextComponent(str))
        }
    }

    override fun hasPermission(permission: String): Boolean {
        return true
    }

}