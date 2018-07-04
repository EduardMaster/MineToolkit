package net.eduard.api.server.minigame;

import org.bukkit.entity.Player;

import net.eduard.api.lib.core.Mine;

public class MinigamePlayer {

	private Player player;
	private MinigamePlayerState state = MinigamePlayerState.NORMAL;
	private MinigameTeam team;
	private MinigameRoom game;
	
	public void leaveTeam() {
		if (hasTeam()) {
			getTeam().leave(this);
		}
	}
	
	public MinigamePlayer(Player player) {
		this.player = player;
	}

	public boolean isState(MinigamePlayerState state) {
		return this.state == state;
	}

	public boolean isPlayingOn(MinigameRoom game) {
		return this.game == game && game.getPlayers().contains(this);
	}

	public MinigamePlayerState getState() {
		return state;
	}

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

	public void join(MinigameTeam team) {
		team.join(this);

	}

	public boolean hasTeam() {
		return team != null;
	}

	public void join(MinigameRoom game) {
		if (!game.getPlayers().contains(this))
			game.getPlayers().add(this);
		this.setGame(game);

	}

	public boolean canBattle(MinigamePlayer player) {
		if (hasTeam()&&player.hasTeam()) {
			if (getTeam().equals(player.getTeam()))
				return false;
		}
		return true;
	}


}
