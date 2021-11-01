package net.eduard.api.server.minigame


/**
 * Representa o lobby do minigame , precisa ser extendido
 */
open class MinigameLobby() {

    var id = 1
    var serverName = "lobby-01"
    var playersAmount = 0
    var playersLimit = 20
    @Transient
    var players = mutableListOf<MinigamePlayer>()




    fun join(player: MinigamePlayer) {
        for (loopPlayer in players) {
            loopPlayer.show(player)
            player.show(loopPlayer)
        }
        player.lobby = this
        if (!players.contains(player))
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
