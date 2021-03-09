package net.eduard.api.lib.bungee

import java.lang.Runnable
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.io.ByteArrayOutputStream
import java.util.stream.Collectors
import java.io.ObjectOutputStream
import java.io.IOException

class BungeeStatusUpdater : Runnable {
    override fun run() {
        updateServers()
    }

    companion object {
        fun updateServers() {
            for (server in BungeeAPI.servers.values) {
                val quantidadeDePlayers = server.count
                val servidor = ProxyServer.getInstance().getServerInfo(server.name) ?: continue
                if (server.isOffline || (servidor.players.size == 0
                            && server.isRestarting)
                ) {
                    servidor.ping { result: ServerPing?, error: Throwable? ->
                        if (result == null) {
                            if (!server.isRestarting) {
                                server.state = ServerState.OFFLINE
                            }
                        } else {
                            if (server.max == 1) {
                                server.max = result.players.max
                            }
                            if (server.isOffline || server.isRestarting) {
                                server.state = ServerState.ONLINE
                            }
                        }
                    }
                }
                server.count = servidor.players.size
                server.players = servidor.players
                    .stream().map { obj: ProxiedPlayer -> obj.name }.collect(Collectors.toList())
                if (quantidadeDePlayers != server.count) {
                    try {
                        ProxyServer.getInstance().console.sendMessage(
                            TextComponent("§aEnviando updates do Servidor " + server.name + " para todos servidores")
                        )
                        val arrayOut = ByteArrayOutputStream()
                        val out = ObjectOutputStream(arrayOut)
                        out.writeUTF(server.name)
                        out.writeUTF("server-update")
                        out.writeObject(server)
                        for (serverInfo in ProxyServer.getInstance().servers.values) {
                            serverInfo.sendData(
                                BungeeAPI.channel,
                                arrayOut.toByteArray()
                            )
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}