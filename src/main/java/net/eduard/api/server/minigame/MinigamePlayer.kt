package net.eduard.api.server.minigame

import net.eduard.api.lib.kotlin.offline
import net.eduard.api.lib.modules.FakePlayer
import net.eduard.api.lib.modules.Mine
import org.bukkit.entity.Player

/**
 * Jogador do Minigame
 *
 * @author Eduard
 */
@Suppress("unused")
class MinigamePlayer() {
    var kills: Int = 0
    var deaths: Int = 0
    var streak: Int = 0
    var state = MinigamePlayerState.NORMAL
    var team: MinigameTeam? = null
    var game: MinigameRoom? = null
    var lobby: MinigameLobby? = null
    var fakePlayer = FakePlayer("Eduard")
    val player get() = fakePlayer.player
    val offline get() = fakePlayer.offline

    val isPlaying
        get() = game != null

    val name get() = fakePlayer.name

    /**
     * Verifica se o jogador esta online
     *
     * @return
     */
    val isOnline: Boolean
        get() = fakePlayer.isOnline

    val isInLobby: Boolean
        get() = lobby != null


    fun show(gamePlayer: MinigamePlayer) {

        send("§aAgora você pode ver §2${gamePlayer.fakePlayer.name}")
        this.player.showPlayer(gamePlayer.player)
    }

    fun hide(gamePlayer: MinigamePlayer) {

        send("§cAgora você não pode ver §7${gamePlayer.fakePlayer.name}")
        this.player.hidePlayer(gamePlayer.player)

    }

    /**
     * Adiciona um Kill
     */
    fun addKill() {
        kills++
    }

    /**
     * Adiciona um Streak
     */
    fun addStreak() {
        streak++
    }

    /**
     * Adiciona uma Morte
     */
    fun addDeath() {
        deaths++
    }

    /**
     * Sai do time atual que esta
     */
    fun leaveTeam() {
        if (hasTeam()) {
            team!!.leave(this)
        }
    }

    /**
     * Verifica o jogador esta neste estado
     *
     * @param state Jogador
     * @return Estado
     */
    fun isState(state: MinigamePlayerState): Boolean {
        return this.state === state
    }

    /**
     * Verifica se o jogador esta jogando na sala
     *
     * @param game Sala
     * @return
     */
    fun isPlayingOn(game: MinigameRoom): Boolean {
        return this.game == game && game.players.contains(this)
    }

    /**
     * Envia a mensagem para o jogador
     *
     * @param message Mensagem
     */
    fun send(message: String) {
        player.sendMessage(Mine.getReplacers(message, player))
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (player == null) 0 else player!!.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null)
            return false
        if (other !is MinigamePlayer) {
            return false;
        }
        return fakePlayer == other.fakePlayer
    }

    /**
     * Força a entrada do jogador no Time
     *
     * @param team
     */
    fun join(team: MinigameTeam) {
        team.join(this)

    }

    /**
     * Verifica se o jogador esta em algum Time
     *
     * @return
     */
    fun hasTeam(): Boolean {
        return team != null
    }

    /**
     *
     * Entrar na Sala
     *
     * @param game Sala
     */
    fun join(game: MinigameRoom) {
        this.game = game
        for (gamePlayerLoop in game.players) {
            gamePlayerLoop.show(this)
            show(gamePlayerLoop)
        }
        if (!game.players.contains(this))
            game.players.add(this)
    }

    /**
     * Verifica se o jogador pode batalhar com o outro jogador
     *
     * @param player Jogador
     * @return
     */
    fun canBattle(player: MinigamePlayer): Boolean {
        if (player == this) return true
        if (hasTeam() && player.hasTeam()) {
            if (team == player.team)
                return false
        }
        return true
    }

    /**
     * Sair da Sala atual
     */
    fun leaveGame() {
        game?.leave(this)

    }

    fun leaveLobby() {
        lobby?.leave(this)
    }

    fun sendMessage(message: String) {
        player.sendMessage(message)
    }

}
