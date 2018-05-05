package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

public class GameTeam {

	private Game game;
	private String name;
	private List<GamePlayer> players = new ArrayList<>();
	private int maxSize;

	public GameTeam() {
		
	}

	public GameTeam(Game game) {
		game.getTeams().add(this);
		setGame(game);
	}


	public int getSize() {
		return players.size();
	}

	public boolean isFull() {
		return players.size() == maxSize;
	}

	public boolean isEmpty() {
		return players.isEmpty();
	}

	public Game getGame() {
		return game;
	}

	public List<Player> getPlaying() {
		return getPlayers(GamePlayerState.NORMAL);
	}

	public void join(GamePlayer player) {
		players.add(player);
		player.setTeam(this);
	}

	public void leave(GamePlayer player) {
		player.setTeam(null);
		players.remove(player);
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<GamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
	}

	public void send(String message) {
		for (GamePlayer p : players) {
			p.send(message);
		}
	}

	public List<Player> getPlayers(GamePlayerState state) {
		List<Player> list = new ArrayList<>();
		for (GamePlayer player : players) {
			if (player.getState() == state) {
				list.add(player.getPlayer());
			}
		}
		return list;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void leaveAll() {
		Iterator<GamePlayer> it = players.iterator();
		while(it.hasNext()) {
			GamePlayer p = it.next();
			leave(p);
		}
		
	}

}
