package net.eduard.api.lib.bungee

import java.io.DataOutputStream
import java.io.IOException
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.io.ByteArrayOutputStream
import java.lang.Exception

class BukkitController : ServerController<Plugin> {
    var plugin: Plugin? = null
    private val listener = BukkitMessageListener(this)
    override fun sendMessage(server: String, tag: String, line: String) {
        val arrayOut = ByteArrayOutputStream()
        val out = DataOutputStream(arrayOut)
        try {
            out.writeUTF(server)
            out.writeUTF(tag)
            out.writeUTF(line)
            val online = firstPlayer ?: return
            // Não usar Bukkit.getServer().sendData()
            // porque essa porra envia 1 requisição para cada jogador online
            // isso significa se tiver 10 on , vai mandar essa Mensagem de bytes , 10 vezes no Bungeecord
            online.sendPluginMessage(plugin, BungeeAPI.channel, arrayOut.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val firstPlayer: Player?
        get() = try {
            Bukkit.getOnlinePlayers().iterator().next()
        } catch (exception: Exception) {
            null
        }

    override fun sendMessage(tag: String, line: String) {
        val arrayOut = ByteArrayOutputStream()
        val out = DataOutputStream(arrayOut)
        try {
            out.writeUTF("bungeecord")
            out.writeUTF(tag)
            out.writeUTF(line)
            val online = firstPlayer ?: return
            online.sendPluginMessage(plugin, BungeeAPI.channel, arrayOut.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun register(pl: Plugin) {
        plugin = pl
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, BungeeAPI.channel)
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BungeeAPI.channel, listener)
        Bukkit.getConsoleSender().sendMessage("§eRegistrando sistema de conexao com Bungeecoard via Plugin Messaging")
    }

    override fun unregister() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, BungeeAPI.channel, listener)
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, BungeeAPI.channel)
    }

    override fun receiveMessage(server: String, tag: String, line: String) {
        for (handler in BungeeAPI.handlers) {
            handler.onMessage(server, tag, line)
        }
    }

    override fun connect(player: String, serverType: String, subType: String, teamSize: Int) {
        sendMessage("connect", "$player $serverType $subType $teamSize")
    }

    override fun setState(server: String, state: ServerState?) {
        sendMessage("set-state", "$server $state")
    }
}