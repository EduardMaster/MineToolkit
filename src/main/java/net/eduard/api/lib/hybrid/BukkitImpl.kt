package net.eduard.api.lib.hybrid


import net.eduard.api.lib.modules.BukkitBungeeAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

object BukkitServer : IServer {

    override fun asyncTask(runnable: Runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().plugins.first(), runnable)
    }

    override fun getPlayer(name: String, uuid: UUID): IPlayer<*> {
        return BukkitPlayer(PlayerUser(name, uuid))
    }

    override fun getPlayer(name: String): IPlayer<Player> {
        return BukkitPlayer(PlayerUser(name, null))
    }

    override fun getPlayer(uuid: UUID): IPlayer<Player> {
        return BukkitPlayer(PlayerUser("Player", uuid))
    }

    override val console: ISender
        get() = BukkitConsole

    override val isBungeecord = false

}

object BukkitConsole : ISender {
    override val name: String
        get() = "BukkitConsole"

    override fun sendMessage(message: String) {
        Bukkit.getConsoleSender().sendMessage(message)
    }

    override fun hasPermission(permission: String) = true
    override fun performCommand(command: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
    }
}


class BukkitPlayer(
 override var offline: PlayerUser
) : IPlayer<Player> {
    override val name: String get() = offline.name
    override val uniquedId: UUID get() = offline.uniqueId!!
    constructor(player: Player) : this(PlayerUser(player.name, player.uniqueId))
    override val server: String
        get() = BukkitBungeeAPI.getCurrentServer()
    override fun playSound(soundName: String) {
        val som = Sound.valueOf(soundName.toUpperCase())
        instance?.playSound(instance?.location, som, 2f, 1f)
    }
    override val isOnline: Boolean
        get() = instance?.isOnline ?: false

    override fun connect(serverName: String) {
        if (instance != null) {
            BukkitBungeeAPI.connectToServer(instance, serverName)
        }
    }
    override fun hashCode(): Int {
        return offline.hashCode()
    }
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is IPlayer<*>) return false
        return other.offline == offline
    }
    override fun sendMessage(message: TextComponent) {
        instance?.spigot()?.sendMessage(message)
    }
    override fun search(): Player? {
        return Bukkit.getPlayerExact(name) ?: Bukkit.getPlayer(uniquedId)
    }

    override var instance: Player? = search()
        get() {
            if (field == null) {
                field = search()
            } else if (!(field!!.isOnline) || !field!!.isValid) {
                field = search()
            }
            return field
        }

    override fun sendMessage(message: String) {
        instance?.sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return instance?.hasPermission(permission) ?: false
    }

    override fun performCommand(command: String) {
        if (instance != null) {
            Bukkit.dispatchCommand(instance, command)
        }
    }

    override fun chat(message: String) {
        instance?.chat(message)
    }


}