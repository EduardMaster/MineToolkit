package net.eduard.api.lib.bungee

import net.md_5.bungee.api.scheduler.ScheduledTask
import java.io.DataOutputStream
import net.md_5.bungee.api.ProxyServer
import java.io.IOException
import net.eduard.api.lib.modules.Extra
import java.util.concurrent.TimeUnit
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Plugin
import java.io.ByteArrayOutputStream

class BungeeController : ServerController<Plugin> {


    private val listener = BungeeMessageListener(this)
    private val updater = BungeeStatusUpdater()
    private var task: ScheduledTask? = null
    override fun sendMessage(server: String, tag: String, line: String) {
        val arrayOut = ByteArrayOutputStream()
        val out = DataOutputStream(arrayOut)
        try {
            out.writeUTF(server)
            out.writeUTF(tag)
            out.writeUTF(line)
            val sv = ProxyServer.getInstance().getServerInfo(server)
            sv?.sendData(BungeeAPI.channel, arrayOut.toByteArray(), true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun sendMessage(tag: String, line: String) {
        for (sv in ProxyServer.getInstance().servers.values) {
            val arrayOut = ByteArrayOutputStream()
            val out = DataOutputStream(arrayOut)
            try {
                out.writeUTF("bungeecord")
                out.writeUTF(tag)
                out.writeUTF(line)
                sv.sendData(BungeeAPI.channel, arrayOut.toByteArray(), true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun receiveMessage(server: String, tag: String, line: String) {
        if (server == "all") {
            sendMessage(tag, line)
            for (handler in BungeeAPI.handlers) {
                handler.onMessage(server, tag, line)
            }
        } else if (server == "bungeecord") {
            if (tag == "connect") {
                val args = line.split(" ").toTypedArray()
                connect(args[0], args[1], args[2], Extra.toInt(args[3]))
            } else {
                for (handler in BungeeAPI.handlers) {
                    handler.onMessage(server, tag, line)
                }
            }
        } else {
            sendMessage(server, tag, line)
        }
    }

    var plugin: Plugin? = null
    override fun register(pl: Plugin) {
        plugin = pl
        ProxyServer.getInstance().registerChannel(BungeeAPI.channel)
        ProxyServer.getInstance().pluginManager.registerListener(plugin, listener)
        task = ProxyServer.getInstance().scheduler.schedule(plugin, updater, 1, 1, TimeUnit.SECONDS)
        BungeeAPI.debug("Registrando sistema no lado do BungeeCord(Proxy)")
    }

    override fun unregister() {
        ProxyServer.getInstance().pluginManager.unregisterListener(listener)
        ProxyServer.getInstance().unregisterChannel(BungeeAPI.channel)
        ProxyServer.getInstance().scheduler.cancel(task)
        BungeeAPI.debug("Desativando sistema no lado do BungeeCord(Proxy)")
    }

    override fun connect(playerName: String, serverType: String, subType: String, teamSize: Int): String {
        val player = ProxyServer.getInstance().getPlayer(playerName)
        if (player != null) {
            ProxyServer.getInstance().console.sendMessage(
                "§cConnecting player " + player.name
                        + " para o servidor do tipo " + serverType + " e subtipo " + subType
            )
            for (spigot in BungeeAPI.servers.values) {
                if (spigot.teamSize == teamSize && spigot.subType.equals(subType, ignoreCase = true)
                    && spigot.type.equals(serverType, ignoreCase = true)
                    && spigot.state === ServerState.ONLINE && spigot.count < spigot.max
                ) {
                    val server = ProxyServer.getInstance()
                        .getServerInfo(spigot.name)
                    player.connect(server)
                    return server.name
                }
            }
        }
        return ""
    }

    override fun setState(serverName: String, state: ServerState?) {
        val server = BungeeAPI.getServer(serverName)
        server.state = state!!
    }
}