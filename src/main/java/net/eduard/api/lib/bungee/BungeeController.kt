package net.eduard.api.lib.bungee

import net.md_5.bungee.api.scheduler.ScheduledTask
import java.io.DataOutputStream
import net.md_5.bungee.api.ProxyServer
import java.io.IOException
import net.eduard.api.lib.modules.Extra
import java.util.concurrent.TimeUnit
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
    override fun register(pluginUsed: Plugin) {
        plugin = pluginUsed
        ProxyServer.getInstance().registerChannel(BungeeAPI.channel)
        ProxyServer.getInstance().pluginManager.registerListener(pluginUsed, listener)
        task = ProxyServer.getInstance().scheduler.schedule(pluginUsed, updater, 1, 1, TimeUnit.SECONDS)
        BungeeAPI.debug("Registrando sistema no lado do BungeeCord(Proxy)")
    }

    override fun unregister() {

        ProxyServer.getInstance().pluginManager.unregisterListener(listener)
        ProxyServer.getInstance().unregisterChannel(BungeeAPI.channel)
        if (task!=null) {
            ProxyServer.getInstance().scheduler.cancel(task)
        }
        BungeeAPI.debug("Desativando sistema no lado do BungeeCord(Proxy)")
    }

    override fun connect(player: String, serverType: String, subType: String, teamSize: Int): String {
        val proxiedPlayer = ProxyServer.getInstance().getPlayer(player)
        if (proxiedPlayer != null) {
            ProxyServer.getInstance().console.sendMessage(
                "§cConnecting player " + proxiedPlayer.name
                        + " para o servidor do tipo " + serverType + " e subtipo " + subType
            )
            for (spigot in BungeeAPI.servers.values) {
                if (spigot.teamSize == teamSize && spigot.subType.equals(subType, ignoreCase = true)
                    && spigot.type.equals(serverType, ignoreCase = true)
                    && spigot.state === ServerState.ONLINE && spigot.count < spigot.max
                ) {
                    val server = ProxyServer.getInstance()
                        .getServerInfo(spigot.name)
                    proxiedPlayer.connect(server)
                    return server.name
                }
            }
        }
        return ""
    }

    override fun setState(server: String, state: ServerState) {
        val serverSpigot = BungeeAPI.getServer(server)
        serverSpigot.state = state
    }
}