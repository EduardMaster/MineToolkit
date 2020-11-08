package net.eduard.api.server.minigame

/**
 * Representa o lobby do minigame , precisa ser extendido
 */
open class MinigameLobby() {

     open var id = 0
     open var maxSlot = 20

    @Transient
    open var players = mutableListOf<MinigamePlayer>()

    open var playersAmount = 0


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
