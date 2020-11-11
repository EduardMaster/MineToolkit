package net.eduard.api.listener

import net.md_5.bungee.api.event.*
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler


@Suppress("unused")
class BungeeEvents : Listener {



    @EventHandler
    fun onJoin(e: PostLoginEvent) {
        val player = e.player

    }

    @EventHandler
    fun event(e: ServerDisconnectEvent) {
        val serverName = e.target.name
        val playerUUID = e.player.uniqueId
        val playerAmount = e.target.players.size


    }
    @EventHandler
    fun event(e: ServerConnectedEvent) {
        val serverName = e.server.info.name
        val playerUUID = e.player.uniqueId
        val playerAmount = e.server.info.players.size

    }

    @EventHandler
    fun onJoin(e: PreLoginEvent) {
        // PendingConnection p = e.getConnection();
        // info("§aPreLoginEvent", p);
    }

    @EventHandler
    fun event(e: ServerConnectEvent) {
    }
    @EventHandler
    fun onJoin(e: LoginEvent) {
        // PendingConnection p = e.getConnection();
        // info("§aLoginEvent", p);
    }

    /**
     * É possivel alterar o sistema de permissão do bungee
     *

     */
    @EventHandler
    fun event(e: PermissionCheckEvent) {
    }

    @EventHandler
    fun event(e: PlayerHandshakeEvent) {
    }

    @EventHandler
    fun event(e: PlayerDisconnectEvent) {
    }

    @EventHandler
    fun event(e: PluginMessageEvent) {
    }

    @EventHandler
    fun event(e: ProxyPingEvent) {
    }

    @EventHandler
    fun event(e: ServerKickEvent) {
    }

    @EventHandler
    fun event(e: ServerSwitchEvent) {
    }

    @EventHandler
    fun event(e: TargetedEvent) {
    }

    @EventHandler
    fun event(e: ProxyReloadEvent) {
    }

    @EventHandler
    fun event(e: TabCompleteResponseEvent) {
    }


    @EventHandler
    fun event(e: TabCompleteEvent) {
    }
}