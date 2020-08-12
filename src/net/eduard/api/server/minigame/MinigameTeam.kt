package net.eduard.api.server.minigame

import org.bukkit.entity.Player

/**
 * Time do Jogador em um Minigame
 *
 * @author Eduard
 */
class MinigameTeam {

    var game: MinigameRoom? = null
    var name: String = "Time1"
    var points: Int = 0
    var players = mutableListOf<MinigamePlayer>()
    var maxSize: Int = 0

    val kills: Int
        get() = players.sumBy { it.kills }

    val deaths: Int
        get() = players.sumBy { it.deaths }


    val size: Int
        get() = players.size

    val isFull: Boolean
        get() = players.size == maxSize

    val isEmpty: Boolean
        get() = players.isEmpty()

    fun addPoint() {
        points++
    }

    constructor()
    constructor(game: MinigameRoom) {
        game.teams.add(this)
        this.game = game
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


    fun getPlayersOnline(state: MinigamePlayerState): List<Player> {
        return getPlayers(state).filter { it.isOnline }.map { it.player }
    }

    fun leaveAll() {
        players.forEach{it.team = null}
        players.clear()
    }

}
