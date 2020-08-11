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
        for (loopPlayer in players) {
            loopPlayer.show(player)
            player.show(loopPlayer)
        }
        player.lobby = this
        players.add(player)
    }

    fun leave(gamePlayer: MinigamePlayer) {
        gamePlayer.lobby = null
        players.remove(gamePlayer)
        for (loopPlayer in players) {
            loopPlayer.hide(gamePlayer)
            gamePlayer.hide(loopPlayer)
        }

    }

}
