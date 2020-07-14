package net.eduard.api.server.minigame

import net.eduard.api.lib.database.annotations.ColumnPrimary
import net.eduard.api.lib.database.annotations.TableName


@TableName("minigame_lobbies")
class MinigameLobby {

    @ColumnPrimary
    var id = 1
    var maxSlot: Int = 20

    @Transient
    var players = mutableListOf<MinigamePlayer>()
    var playersAmount = players.size
    fun join(player: MinigamePlayer) {
        for (p in players) {
            p.show(player)
            player.show(p)
        }
        player.lobby = this
        if (!players.contains(player))
            players.add(player)

    }

    fun leave(player: MinigamePlayer) {
        for (p in players) {
            p.hide(player)
            player.hide(p)
        }
        player.lobby = null
        players.remove(player)
    }

}
