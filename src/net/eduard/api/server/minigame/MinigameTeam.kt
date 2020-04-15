package net.eduard.api.server.minigame

import java.util.ArrayList
import java.util.stream.Collectors

import org.bukkit.entity.Player

/**
 * Time do Jogador em um Minigame
 *
 * @author Eduard
 */
class MinigameTeam {

    var game: MinigameRoom? = null
    var name: String? = null
    var points: Int = 0
    private var players: MutableList<MinigamePlayer> = ArrayList()
    var maxSize: Int = 0

    //		int kills = 0;
    //		for ( MinigamePlayer player : players) {
    //			kills+=player.getKills();
    //		}
    //		return players.stream().reduce(0, new BiFunction<Integer, MinigamePlayer, Integer>() {
    //
    //			@Override
    //			public Integer apply(Integer t, MinigamePlayer u) {
    //
    //				return t+=u.getKills();
    //			}
    //		},new BinaryOperator<Integer>() {
    //
    //			@Override
    //			public Integer apply(Integer t, Integer u) {
    //
    //				return null;
    //			}
    //
    //
    //		} );
    val kills: Int
        get() {
            return players.sumBy { it.kills }

          // players.stream().reduce(0, { n, p -> n += p.kills }, null);
        }
    val deaths: Int
        get() =  players.sumBy { it.deaths } //players.stream().reduce(0, { n, p -> n += p.deaths }, null)

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

    fun getPlayers(): List<MinigamePlayer> {
        return players
    }

    fun setPlayers(players: MutableList<MinigamePlayer>) {
        this.players = players
    }

    fun send(message: String) {
        for (p in players) {
            p.send(message)
        }
    }

    fun getPlayers(state: MinigamePlayerState): List<MinigamePlayer> {
        return players.filter { it.state==state }.toList()
        //return players.stream().filter { p -> p.state == state }.collect<List<MinigamePlayer>, Any>(Collectors.toList())
    }

    fun getPlayersOnline(state: MinigamePlayerState): List<Player> {
        val list = ArrayList<Player>()
        for (player in players) {
            if (player.state == state) {
                if (player.isOnline) {
                    list.add(player.player!!)
                }
            }
        }
        return list
    }

    fun leaveAll() {
        val it = players.iterator()
        while (it.hasNext()) {
            val p = it.next()
            leave(p)
        }

    }

}
