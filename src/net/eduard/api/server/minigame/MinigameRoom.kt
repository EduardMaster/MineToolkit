package net.eduard.api.server.minigame

import net.eduard.api.lib.modules.Extra
import java.util.ArrayList

import org.bukkit.entity.Player

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.storage.Storable
import net.eduard.api.lib.storage.Storable.*
import kotlin.streams.toList

/**
 * Sala do Minigame
 *
 * @author Eduard-PC
 */
class MinigameRoom {

    @Transient
    lateinit var minigame: Minigame

    @StorageAttributes(reference = true)
    var map: MinigameMap = MinigameMap()

    constructor()

    constructor(minigame: Minigame, map: MinigameMap) {
        this.minigame = minigame
        this.id = minigame.rooms.size + 1
        this.minigame.rooms.add(this)
        this.map = map
        this.isEnabled = true
        this.time = minigame.timeIntoStart
    }

    var mode = MinigameMode.NORMAL
    var id: Int = 0
    var time: Int = 0
    var isEnabled: Boolean = false
    var round: Int = 0

    @Transient
    var state = MinigameState.STARTING

    @Transient
    var mapUsed: MinigameMap = map

    var secondWinner: MinigamePlayer? = null
    var thirdWinner: MinigamePlayer? = null

    @Transient
    var players = mutableListOf<MinigamePlayer>()

    @Transient
    var losers = mutableListOf<MinigamePlayer>()

    @Transient
    var teams = mutableListOf<MinigameTeam>()

    val playersOnline: List<Player>
        get() = players.filter { it.isOnline }.map { it.player!! }

    val teamWinner: MinigameTeam?
        get() = teams.firstOrNull { it.getPlayers(MinigamePlayerState.NORMAL).isNotEmpty() }


    val winner: MinigamePlayer
        get() = getPlayers(MinigamePlayerState.NORMAL)[0]


    /**
     * Manda a mensagem para todos os jogadores participando da Sala
     *
     * @param message
     */
    fun broadcast(message: String) {
        for (player in players) {

            player.send(minigame.messagePrefix + message.replace("\$time", Extra.formatSeconds1(time)).replace("\$max", "" + map!!.maxPlayersAmount)
                    .replace("\$players", "" + players.size))
        }
    }

    fun hasMinPlayersAmount(): Boolean {
        return players.size >= map!!.minPlayersAmount
    }

    /**
     * Verifica se o jogador esta jogando nesta Sala
     *
     * @param player Jogador
     * @return
     */
    fun isPlaying(player: Player): Boolean {
        return players.stream().filter { p -> p.player == player }.findFirst().isPresent
    }

    fun getTeams(state: MinigamePlayerState): List<MinigameTeam> {
        return teams.filter {  it.getPlayers(state).isNotEmpty() }
    }

    fun getPlayersOnline(state: MinigamePlayerState): List<Player> {

        return  players.filter { p -> p.state == state && p.isOnline }.map { it.player }
    }

    fun getPlayers(state: MinigamePlayerState): List<MinigamePlayer> {
        return players.filter { p -> p.state == state }
    }

    fun checkEnd(): Boolean {
        return time == minigame.timeIntoGameOver
    }

    fun checkWinner(): Boolean {
        return getPlayers(MinigamePlayerState.NORMAL).any()
    }

    fun checkTeamWinner(): Boolean {

        return teams.any { it.getPlayers(MinigamePlayerState.NORMAL).isNotEmpty() }
    }

    fun checkForceStart(): Boolean {
        return players.size >= map.neededPlayersAmount && time > minigame.timeOnForceTimer
    }

    fun forceGameStart() {
        time = minigame.timeOnForceTimer
    }

    /**
     * Aumenta 1 segundo na contagem
     *
     * @return
     */
    fun advance(): Int {
        return ++time
    }

    /**
     * Diminui 1 segundo da contagem
     *
     * @return
     */
    fun decrease(): Int {
        return --time
    }

    /**
     * Coloca o estado desta sala em Jogando (A batalha vai começar)
     */
    fun startGame() {
        time = minigame.timeOnStartTimer
        state = MinigameState.PLAYING
    }

    /**
     * Coloca o estado desta sala em Equipando (Pre jogo de muitos minigames)
     */
    fun startPreGame() {
        time = minigame.timeIntoPlay
        state = MinigameState.EQUIPPING
    }

    /**
     * Coloca o estado desta sala em Acabando (estado usado em alguns eventos
     * apenas)
     */
    fun ending() {
        time = minigame.timeOnRestartTimer
        state = MinigameState.ENDING
    }

    /**
     * Coloca o estado desta sala em Reiniciando <br></br>
     * 'quando fazer eventos use este estado para saber que o evento esta desligado'
     */
    fun restarting() {
        state = MinigameState.RESTARTING
        time = minigame.timeIntoRestart
    }

    /**
     * Coloca o estado desta sala em Iniciando
     */
    fun restart() {
        state = MinigameState.STARTING
        time = minigame.timeIntoStart
    }

    /**
     * Remove o jogador desta Sala
     *
     * @param player
     */
    fun leave(player: MinigamePlayer) {
        players.remove(player)
        player.game = null
        for (jogador in players) {
            jogador.hide(player)
            player.hide(jogador)
        }
    }

    fun leaveAll() {
        players.forEach{ it.game = null}
        players.clear()

    }


    fun isState(state: MinigameState): Boolean {
        return this.state === state
    }

    /**
     * Remove os jogadores de todos os Times
     */
    fun emptyTeams() {
        for (team in teams) {
            team.leaveAll()
        }
    }

    /**
     * Força a entrada do jogador na Sala
     *
     * @param player Jogador
     */
    fun join(player: MinigamePlayer) {
        player.join(this)
    }

    /**
     * Verifica se tem Espaço na sala para novos jogadores
     *
     * @return
     */
    fun hasSpace(): Boolean {
        return players.size < map!!.maxPlayersAmount
    }

}