package net.eduard.api.lib.command

import net.eduard.api.lib.bungee.BukkitBungeeAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.entity.Player

class PlayerBukkit(override val player: Player) : PlayerOnline<Player>(player.name,player.uniqueId) {

    override fun sendMessage(str: String) {
        player.sendMessage(str)
    }

    override fun sendMessage(str: TextComponent) {
        player.spigot().sendMessage(str)
    }

    override fun connect(serverName: String) {
        BukkitBungeeAPI.connectToServer(player, serverName)
    }

    override var fly: Boolean
        get() = player.allowFlight
        set(value) {
            player.allowFlight = true
        }

    override fun hasPermission(permission: String): Boolean {
        return player.hasPermission(permission)
    }


}