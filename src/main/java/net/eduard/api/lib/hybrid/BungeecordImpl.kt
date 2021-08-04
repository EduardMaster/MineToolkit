package net.eduard.api.lib.hybrid

import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*

object BungeeServer : IServer{
    override fun getPlayer(name: String, uuid: UUID): IPlayer<*> {
        return BungeePlayer(name,uuid)
    }

    override fun getPlayer(name: String): IPlayer<ProxiedPlayer> {
        return BungeePlayer(name,UUID.nameUUIDFromBytes(("OfflinePlayer:$name").toByteArray()))
    }

    override fun getPlayer(uuid: UUID): IPlayer<ProxiedPlayer> {
        return BungeePlayer("Player",uuid)
    }

    override val console: ISender
        get() = BungeeConsole
    override val isBungeecord = true

}

object BungeeConsole : ISender{
    override val name: String
        get() = "BungeeConsole"

    override fun sendMessage(message: String) {
        ProxyServer.getInstance().console.sendMessage(TextComponent(message))
    }

    override fun hasPermission(permission: String) = true
    override fun performCommand(command: String) {
        ProxyServer.getInstance().pluginManager
            .dispatchCommand(ProxyServer.getInstance().console
            , command)
    }
}


class BungeePlayer(
override var name: String,
override var uuid: UUID) : IPlayer<ProxiedPlayer>{

    constructor(player : ProxiedPlayer) : this(player.name,
        player.uniqueId)

    override fun hashCode(): Int {
        return offline.hashCode()
    }

    override val server: String
        get() = instance?.server?.info?.name ?:"lobby"

    override fun playSound(soundName: String) {

    }
    override fun equals(other: Any?): Boolean {
        if (other == null)return false
        if (this === other) return true
        if (other !is IPlayer<*>) return false
        return other.offline == offline
    }
    override val isOnline: Boolean
        get() = instance?.isConnected?:false

    override fun connect(serverName: String) {
        val server =ProxyServer.getInstance().getServerInfo(serverName)?:return
        instance?.connect(server)
    }

    override fun search(): ProxiedPlayer? {
       val trying = ProxyServer.getInstance().getPlayer(name)
        return trying ?: ProxyServer.getInstance().getPlayer(uuid)
    }
    override var instance: ProxiedPlayer? = search()
    get(){
        if (field==null){
            field = search()
        }else if (!field!!.isConnected){
            field = search()
        }
       return field
    }

    override fun sendMessage(message: String) {
       instance?.sendMessage(TextComponent(message))
    }

    override fun sendMessage(message: TextComponent) {
        instance?.sendMessage(message)
    }
    override fun hasPermission(permission: String): Boolean {
        return instance?.hasPermission(permission)?: false
    }

    override fun performCommand(command: String) {
        if (instance!=null) {
            ProxyServer.getInstance().pluginManager
                .dispatchCommand(
                   instance
                    , command
                )
        }
    }

    override fun chat(message: String) {
        instance?.chat(message)
    }

}