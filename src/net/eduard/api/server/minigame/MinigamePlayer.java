package net.eduard.api.server.minigame;

import net.eduard.api.lib.storage.StorageAttributes;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;

/**
 * Jogador do Minigame
 * 
 * @author Eduard
 *
 */
@StorageAttributes(indentificate = true)
public class MinigamePlayer {

	private Player player;
	private int kills;
	private int deaths;
	private int streak;
	private MinigamePlayerState state = MinigamePlayerState.NORMAL;
	private MinigameTeam team;
	private MinigameRoom game;
	private MinigameLobby lobby;

	public void show(MinigamePlayer player) {
		if (player.equals(this))
			return;
		getPlayer().showPlayer(player.getPlayer());
	}

	public void hide(MinigamePlayer player) {
		if (player.equals(this))
			return;
		getPlayer().hidePlayer(player.getPlayer());

	}

	/**
	 * Adiciona um Kill
	 */
	public void addKill() {
		setKills(getKills() + 1);
	}

	/**
	 * Adiciona um Streak
	 */
	public void addStreak() {
		setStreak(getStreak() + 1);
	}

	/**
	 * Adiciona uma Morte
	 */
	public void addDeath() {
		setDeaths(getDeaths() + 1);
	}

	/**
	 * Sai do time atual que esta
	 */
	public void leaveTeam() {
		if (hasTeam()) {
			getTeam().leave(this);
		}
	}

	public MinigamePlayer(Player player) {
		this.player = player;
	}

	/**
	 * Verifica o jogador esta neste estado
	 * 
	 * @param state Jogador
	 * @return Estado
	 */
	public boolean isState(MinigamePlayerState state) {
		return this.state == state;
	}

	/**
	 * Verifica se o jogador esta jogando na sala
	 * 
	 * @param game Sala
	 * @return
	 */
	public boolean isPlayingOn(MinigameRoom game) {
		return this.game == game && game.getPlayers().contains(this);
	}

	public MinigamePlayerState getState() {
		return state;
	}

	/**
	 * Envia a mensagem para o jogador
	 * 
	 * @param message Mensagem
	 */
	public void send(String message) {
		player.sendMessage(Mine.getReplacers(message, player));
	}

	public void setState(MinigamePlayerState state) {
		this.state = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public MinigameRoom getGame() {
		return game;
	}

	public void setGame(MinigameRoom game) {
		this.game = game;
	}

	public boolean isPlaying() {
		return game != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MinigamePlayer other = (MinigamePlayer) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}

	public MinigameTeam getTeam() {
		return team;
	}

	public void setTeam(MinigameTeam team) {
		this.team = team;
	}

	/**
	 * Força a entrada do jogador no Time
	 * 
	 * @param team
	 */
	public void join(MinigameTeam team) {
		team.join(this);

	}

	/**
	 * Verifica se o jogador esta em algum Time
	 * 
	 * @return
	 */
	public boolean hasTeam() {
		return team != null;
	}

	/**
	 * 
	 * Entrar na Sala
	 * 
	 * @param game Sala
	 */
	public void join(MinigameRoom game) {
		if (!game.getPlayers().contains(this))
			game.getPlayers().add(this);
		this.setGame(game);
		for (MinigamePlayer jogador : game.getPlayers()) {
			jogador.show(this);
			show(jogador);
		}

	}

	/**
	 * Verifica se o jogador pode batalhar com o outro jogador
	 * 
	 * @param player Jogador
	 * @return
	 */
	public boolean canBattle(MinigamePlayer player) {
		if (hasTeam() && player.hasTeam()) {
			if (getTeam().equals(player.getTeam()))
				return false;
		}
		return true;
	}

	/**
	 * Sair da Sala atual
	 */
	public void leaveGame() {
		if (isPlaying()) {
			getGame().leave(this);
		}

	}

	/**
	 * Verifica se o jogador esta online
	 * 
	 * @return
	 */
	public boolean isOnline() {

		return getPlayer() != null;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getStreak() {
		return streak;
	}

	public void setStreak(int streak) {
		this.streak = streak;
	}

	public MinigameLobby getLobby() {
		return lobby;
	}

	public void setLobby(MinigameLobby lobby) {
		this.lobby = lobby;
	}

	public boolean isInLobby() {

		return lobby != null;
	}

	public void leaveLobby() {
		if (isInLobby()) {
			lobby.leave(this);
		}
		
	}

}
