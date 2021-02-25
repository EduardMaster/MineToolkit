package net.eduard.api.lib.hybrid


import net.eduard.api.lib.modules.BukkitBungeeAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

object BukkitServer : IServer{
    override fun getPlayer(name: String, uuid: UUID): IPlayer<*> {
        return BukkitPlayer(name,uuid)
    }

    override fun getPlayer(name: String): IPlayer<Player> {
        return BukkitPlayer(name,UUID.nameUUIDFromBytes(("OfflinePlayer:$name").toByteArray()))
    }

    override fun getPlayer(uuid: UUID): IPlayer<Player> {
        return BukkitPlayer("Player",uuid)
    }

    override val console: ISender
        get() = BukkitConsole
    override val isBungeecord = false

}

object BukkitConsole : ISender{
    override val name: String
        get() = "BukkitConsole"

    override fun sendMessage(message: String) {
       Bukkit.getConsoleSender().sendMessage(message)
    }

    override fun hasPermission(permission: String) = true
    override fun performCommand(command: String) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command)
    }
}


class BukkitPlayer(
override var name: String,
override var uuid: UUID) : IPlayer<Player>{

    constructor(player : Player) : this(player.name,
        player.uniqueId)

    override val isOnline: Boolean
        get() = instance?.isOnline?:false

    override fun connect(serverName: String) {

        if (instance!=null) {
           BukkitBungeeAPI.connectToServer(instance, serverName)
        }

    }


    override fun hashCode(): Int {
        return offline.hashCode()
    }
    override fun equals(other: Any?): Boolean {
        if (other == null)return false
        if (this === other) return true
        if (other !is IPlayer<*>) return false
        return other.offline == offline
    }

    override fun sendMessage(message: TextComponent) {
        instance?.spigot()?.sendMessage(message)
    }
    override fun search(): Player? {
       val trying = Bukkit.getPlayer(name)
        return trying ?: Bukkit.getPlayer(uuid)
    }
    override var instance: Player? = search()
    get(){
        if (field==null){
            field = search()
        }else if (!(field!!.isOnline) || !field!!.isValid){
            field = search()
        }
       return field
    }

    override fun sendMessage(message: String) {
       instance?.sendMessage(message)
    }

    override fun hasPermission(permission: String): Boolean {
        return instance?.hasPermission(permission)?: false
    }

    override fun performCommand(command: String) {
        if (instance!= null) {
            Bukkit.dispatchCommand(instance, command)
        }
    }

    override fun chat(message: String) {
       instance?.chat(message)
    }


}