package net.eduard.api.listener

import net.eduard.api.EduardAPIBungee
import net.md_5.bungee.BungeeCord
import net.md_5.bungee.api.event.*
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler


@SuppressWarnings("unused")
class BungeeEvents : Listener {


    val bungee get() = EduardAPIBungee.instance.bungee
    val db get() = EduardAPIBungee.instance.db




    @EventHandler
    fun onJoin(e: PostLoginEvent) {
        val player = e.player
        // info("§aPostLoginEvent", player.getPendingConnection());
        if (db.hasConnection()) {
            if (!bungee.playersContains(player.name)) {
                db.insert("players", player.name, player.uniqueId, "")
            }
        }
    }

    @EventHandler
    fun event(e: ServerDisconnectEvent) {
        val serverName = e.target.name
        val playerUUID = e.player.uniqueId
        val playerAmount = e.target.players.size
        if (db.hasConnection()) {
            BungeeCord.getInstance().scheduler.runAsync(EduardAPIBungee.instance.plugin) {
                bungee.setPlayersAmount(serverName, playerAmount)
                bungee.setPlayerServer(playerUUID, "")
            }
        }
    }
    @EventHandler
    fun event(e: ServerConnectedEvent) {
        val serverName = e.server.info.name
        val playerUUID = e.player.uniqueId
        val playerAmount = e.server.info.players.size
        if (db.hasConnection()) {
            BungeeCord.getInstance().scheduler.runAsync(EduardAPIBungee.instance.plugin) {
                bungee.setPlayersAmount(serverName, playerAmount)
                bungee.setPlayerServer(playerUUID, serverName)
            }
        }
    }

    @EventHandler
    fun onJoin(e: PreLoginEvent) {
        // PendingConnection p = e.getConnection();
        // info("§aPreLoginEvent", p);
    }

    @EventHandler
    private fun event(e: ServerConnectEvent) {
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
    private fun event(e: PermissionCheckEvent) {
    }

    @EventHandler
    private fun event(e: PlayerHandshakeEvent) {
    }

    @EventHandler
    private fun event(e: PlayerDisconnectEvent) {
    }

    @EventHandler
    private fun event(e: PluginMessageEvent) {
    }

    @EventHandler
    private fun event(e: ProxyPingEvent) {
    }

    @EventHandler
    private fun event(e: ServerKickEvent) {
    }

    @EventHandler
    private fun event(e: ServerSwitchEvent) {
    }

    @EventHandler
    private fun event(e: TargetedEvent) {
    }

    @EventHandler
    private fun event(e: ProxyReloadEvent) {
    }

    @EventHandler
    private fun event(e: TabCompleteResponseEvent) {
    }


    @EventHandler
    private fun event(e: TabCompleteEvent) {
    }
}