package net.eduard.api.server.minigame

import net.eduard.api.lib.game.FakePlayer
import org.bukkit.entity.Player

import net.eduard.api.lib.modules.Mine

/**
 * Jogador do Minigame
 *
 * @author Eduard
 */
class MinigamePlayer() {
    var kills: Int = 0
    var deaths: Int = 0
    var streak: Int = 0
    var state = MinigamePlayerState.NORMAL
    var team: MinigameTeam? = null
    var game: MinigameRoom? = null
    var lobby: MinigameLobby? = null
     var player : Player? = null
    var fakePlayer : FakePlayer? = null

    val isPlaying: Boolean
        get() = game != null

    /**
     * Verifica se o jogador esta online
     *
     * @return
     */
    val isOnline: Boolean
        get() = player != null

    val isInLobby: Boolean
        get() = lobby != null

    fun show(player: MinigamePlayer) {
        if (player == this)
            return
        this.player!!.showPlayer(player.player)
    }

    fun hide(player: MinigamePlayer) {
        if (player == this)
            return
        this.player!!.hidePlayer(player.player)

    }

    /**
     * Adiciona um Kill
     */
    fun addKill() {
        kills = kills + 1
    }

    /**
     * Adiciona um Streak
     */
    fun addStreak() {
        streak = streak + 1
    }

    /**
     * Adiciona uma Morte
     */
    fun addDeath() {
        deaths = deaths + 1
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
        player!!.sendMessage(Mine.getReplacers(message, player))
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (player == null) 0 else player!!.hashCode()
        return result
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as MinigamePlayer?
        if (player == null) {
            if (other!!.player != null)
                return false
        } else if (player != other!!.player)
            return false
        return true
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
        if (!game.players.contains(this))
            game.players.add(this)
        this.game = game
        for (jogador in game.players) {
            jogador.show(this)
            show(jogador)
        }

    }

    /**
     * Verifica se o jogador pode batalhar com o outro jogador
     *
     * @param player Jogador
     * @return
     */
    fun canBattle(player: MinigamePlayer): Boolean {
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
        if (isPlaying) {
            game!!.leave(this)
        }

    }

    fun leaveLobby() {
        if (isInLobby) {
            lobby!!.leave(this)
        }

    }

}
