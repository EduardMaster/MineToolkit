package net.eduard.api.lib.command

import net.eduard.api.lib.modules.BukkitBungeeAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerBukkit( bukkitPlayer: Player)
    : PlayerOnline<Player>(bukkitPlayer.name,bukkitPlayer.uniqueId) {
    override var player: Player = bukkitPlayer
    get(){
        if (!field.isOnline){
            field = Bukkit.getPlayer(name)
        }
        return field
    }
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

    override val isOffline: Boolean
        get() = !player.isOnline


}