package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
/**
 * Time do Jogador em um Minigame
 * @author Eduard
 *
 */
public class MinigameTeam {

	private MinigameRoom game;
	private String name;
	private List<MinigamePlayer> players = new ArrayList<>();
	private int maxSize;

	public MinigameTeam() {
		
	}

	public MinigameTeam(MinigameRoom game) {
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

	public MinigameRoom getGame() {
		return game;
	}

	public void join(MinigamePlayer player) {
		players.add(player);
		player.setTeam(this);
	}

	public void leave(MinigamePlayer player) {
		player.setTeam(null);
		players.remove(player);
	}

	public void setGame(MinigameRoom game) {
		this.game = game;
	}

	public List<MinigamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<MinigamePlayer> players) {
		this.players = players;
	}

	public void send(String message) {
		for (MinigamePlayer p : players) {
			p.send(message);
		}
	}
	public List<MinigamePlayer> getPlayers(MinigamePlayerState state) {
		return players.stream().filter(p->p.getState() == state).collect(Collectors.toList());
	}

	public List<Player> getPlayersOnline(MinigamePlayerState state) {
		List<Player> list = new ArrayList<>();
		for (MinigamePlayer player : players) {
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
		Iterator<MinigamePlayer> it = players.iterator();
		while(it.hasNext()) {
			MinigamePlayer p = it.next();
			leave(p);
		}
		
	}

}
