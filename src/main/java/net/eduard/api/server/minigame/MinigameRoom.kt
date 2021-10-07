package net.eduard.api.server.minigame

import net.eduard.api.lib.database.annotations.ColumnPrimary
import net.eduard.api.lib.modules.Extra

import org.bukkit.entity.Player

import net.eduard.api.lib.storage.annotations.StorageAttributes
import net.eduard.api.lib.storage.annotations.StorageIndex
import net.eduard.api.lib.storage.annotations.StorageReference
import org.bukkit.Bukkit

/**
 * Sala do Minigame
 *
 * @author Eduard-PC
 */
@Suppress("unused")
open class MinigameRoom() {

    @Transient
    lateinit var minigame: Minigame

    @StorageReference
    lateinit var mode: MinigameMode

    @StorageIndex
    @ColumnPrimary
    var id: Int = 0

    @StorageReference
    var map: MinigameMap = MinigameMap()



    open fun start() {
        mapUsed = map.copy()
        mapUsed.minigame = minigame
        mapUsed.worldName = "worlds/${minigame.name}/room/$id"
        if (minigame.worldBased) {
            mapUsed.copyWorld(map)
           // mapUsed.setupChests()
        } else {
            mapUsed.clearWorld()
            mapUsed.paste(mapUsed.feastCenter!!)
        }
        restart()

    }

    open fun stop() {
        mapUsed.unloadWorld()
        restarting()
    }

    open fun reset() {
        if (minigame.worldBased) {
            mapUsed.resetWorld()
           // mapUsed.setupChests()
        } else {
            if (minigame.schematicBasedRestartWorld)
                mapUsed.clearWorld()
            mapUsed.paste(mapUsed.feastCenter!!)
        }
    }

    var isEnabled: Boolean = false
    var ip: String = "127.0.0.1"
    var port: Int = Bukkit.getPort()
    var round: Int = 0
    var state = MinigameState.STARTING
    var time: Int = 0
    var teamSize = 1
    var maxRounds = 3

    @Transient
    var stats = mutableMapOf<MinigamePlayer, MinigamePlayerStats>()

    @Transient
    var mapUsed: MinigameMap = map

    @Transient
    var players = mutableListOf<MinigamePlayer>()

    @Transient
    var losers = mutableListOf<MinigamePlayer>()

    @Transient
    var teams = mutableListOf<MinigameTeam>()

    val playersOnline: List<Player>
        get() = players.filter { it.isOnline }.map { it.player!! }

    val teamWinner: MinigameTeam?
        get() = teams.firstOrNull {
            it.getPlayers(MinigamePlayerState.NORMAL)
                .isNotEmpty()
        }


    val winner: MinigamePlayer
        get() = getPlayers(MinigamePlayerState.NORMAL)[0]


    /**
     * Manda a mensagem para todos os jogadores participando da Sala
     *
     * @param message
     */
    fun broadcast(message: String) {
        for (player in players) {
            player.send(
                minigame.messagePrefix + message
                    .replace("%time", Extra.formatSeconds1(time))
                    .replace("%max", "" + map.maxPlayersAmount)
                    .replace("%players", "" + players.size)
            )
        }
    }

    fun hasMinPlayersAmount(): Boolean {
        return getPlayers(MinigamePlayerState.NORMAL).size >= map.minPlayersAmount
    }

    /**
     * Verifica se o jogador esta jogando nesta Sala
     *
     * @param player Jogador
     * @return
     */
    fun isPlaying(player: Player): Boolean {
        return players.any { minigamePlayer -> minigamePlayer.player == player }

    }

    fun getTeams(state: MinigamePlayerState): List<MinigameTeam> {
        return teams.filter {
            it.getPlayers(state)
                .isNotEmpty()
        }
    }

    fun getPlayersOnline(state: MinigamePlayerState): List<Player> {
        return players.filter { p -> p.state == state && p.isOnline }
            .map { it.player }
    }

    fun getPlayers(state: MinigamePlayerState): List<MinigamePlayer> {
        return players.filter { p -> p.state == state }
    }

    fun checkEnd(): Boolean {
        return time == mode.timeIntoGameOver
    }

    fun checkWinner(): Boolean {
        return getPlayers(MinigamePlayerState.NORMAL).size == 1
    }

    fun checkTeamWinner(): Boolean {
        return teams.filter {
            it.getPlayers(MinigamePlayerState.NORMAL)
                .isNotEmpty()
        }.size == 1
    }

    fun checkForceStart(): Boolean {
        return players.size >= map.neededPlayersAmount &&
                time > mode.timeOnForceTimer
    }

    fun forceGameStart() {
        time = mode.timeOnForceTimer
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
        time = mode.timeOnStartTimer
        state = MinigameState.PLAYING
    }

    /**
     * Coloca o estado desta sala em Equipando (Pre jogo de muitos minigames)
     */
    fun startPreGame() {
        time = mode.timeIntoPlay
        state = MinigameState.EQUIPPING
    }

    /**
     * Coloca o estado desta sala em Acabando (estado usado em alguns eventos
     * apenas)
     */
    fun ending() {
        time = mode.timeOnRestartTimer
        state = MinigameState.ENDING
    }

    /**
     * Coloca o estado desta sala em Reiniciando <br></br>
     * 'quando fazer eventos use este estado para saber que o evento esta desligado'
     */
    fun restarting() {
        state = MinigameState.RESTARTING
        time = mode.timeIntoRestart
    }

    /**
     * Coloca o estado desta sala em Iniciando
     */
    fun restart() {
        state = MinigameState.STARTING
        time = mode.timeIntoStart
        stats.clear()
    }

    /**
     * Remove o jogador desta Sala
     *
     * @param player
     */
    open fun leave(player: MinigamePlayer) {
        players.remove(player)
        player.resetStats()
        player.game = null
        for (jogador in players) {
            jogador.hide(player)
            player.hide(jogador)
        }
    }

    fun leaveAll() {
        players.forEach { it.game = null }
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
        teams.clear()
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
        return getPlayers(MinigamePlayerState.NORMAL).size < map.maxPlayersAmount
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MinigameRoom

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}