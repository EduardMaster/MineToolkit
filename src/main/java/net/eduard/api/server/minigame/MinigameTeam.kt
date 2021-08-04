package net.eduard.api.server.minigame

/**
 * Representa o Time do Jogador em um Minigame
 *
 * @author Eduard
 */
class MinigameTeam(
    var game: MinigameRoom
){

    var name: String = "Time1"
    var points = 0
    var players = mutableListOf<MinigamePlayer>()
    var maxSize = 0

    val kills: Int
        get() = players.sumBy { it.stats.kills }

    val deaths: Int
        get() = players.sumBy { it.stats.deaths }


    val size: Int
        get() = players.size

    val isFull: Boolean
        get() = players.size == maxSize

    val isEmpty: Boolean
        get() = players.isEmpty()

    fun addPoint() {
        points++
    }


    fun join(player: MinigamePlayer) {
        players.add(player)
        player.team = this
    }

    fun leave(player: MinigamePlayer) {
        player.team = null
        players.remove(player)
    }

    fun send(message: String) {
        for (gamePlayer in players) {
            gamePlayer.send(message)
        }
    }

    fun getPlayers(state: MinigamePlayerState): List<MinigamePlayer> {
        return players.filter { it.state == state }
    }

    fun leaveAll() {
        players.forEach{it.team = null}
        players.clear()
    }

}
