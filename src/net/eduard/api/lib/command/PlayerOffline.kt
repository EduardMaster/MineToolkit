package net.eduard.api.lib.command

import net.md_5.bungee.BungeeCord
import org.bukkit.Bukkit
import java.lang.Exception
import java.util.*

class PlayerOffline(var name: String = "Eduard",
                    var uniqueId: UUID = UUID.nameUUIDFromBytes(("OfflinePlayer:$name").toByteArray())) {

    private var onlinePlayer: PlayerSender<*>? = null

    constructor(player: PlayerSender<*>) : this(player.name, player.uniqueId) {
        this.onlinePlayer = player
    }


    fun online(): PlayerSender<*> {
        if (onlinePlayer == null) {
            try {
                onlinePlayer = PlayerBukkit(Bukkit.getPlayer(name))
            } catch (er: Exception) {
                onlinePlayer = PlayerBungee(BungeeCord.getInstance().getPlayer(name))
            }
        }
        return onlinePlayer!!
    }

    override fun equals(other: Any?): Boolean {
        if (other == null)return false
        if (this === other) return true
        if (other is PlayerOffline) {
            return (name.equals(other.name, true))
        }
        return true
    }

    override fun hashCode(): Int {
        return name.toLowerCase().hashCode()
    }


}