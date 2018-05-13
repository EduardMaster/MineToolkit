package net.eduard.api.server.minigame;

import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Mine;

public class GamePlayer {

	private Player player;
	private GamePlayerState state = GamePlayerState.NORMAL;
	private GameTeam team;
	private Game game;
	
	public void leaveTeam() {
		if (hasTeam()) {
			getTeam().leave(this);
		}
	}
	
	public GamePlayer(Player player) {
		this.player = player;
	}

	public boolean isState(GamePlayerState state) {
		return this.state == state;
	}

	public boolean isPlayingOn(Game game) {
		return this.game == game && game.getPlayers().contains(this);
	}

	public GamePlayerState getState() {
		return state;
	}

	public void send(String message) {
		player.sendMessage(Mine.getReplacers(message, player));
	}

	public void setState(GamePlayerState state) {
		this.state = state;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
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
		GamePlayer other = (GamePlayer) obj;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}

	public GameTeam getTeam() {
		return team;
	}

	public void setTeam(GameTeam team) {
		this.team = team;
	}

	public void join(GameTeam team) {
		team.join(this);

	}

	public boolean hasTeam() {
		return team != null;
	}

	public void join(Game game) {
		if (!game.getPlayers().contains(this))
			game.getPlayers().add(this);
		this.setGame(game);

	}

	public boolean canBattle(GamePlayer player) {
		if (hasTeam()&&player.hasTeam()) {
			if (getTeam().equals(player.getTeam()))
				return false;
		}
		return true;
	}


}
