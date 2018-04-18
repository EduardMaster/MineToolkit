package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import net.eduard.api.setup.StorageAPI.Reference;

/**
 * Sala do Minigame
 * 
 * @author Eduard-PC
 *
 */
public class Game {

	private int id;
	private int time;
	private Minigame minigame;
	private boolean enabled;

	@Reference
	private GameMap map;
	private MinigameState state = MinigameState.STARTING;
	private List<GamePlayer> players = new ArrayList<>();
	private List<GameTeam> teams = new ArrayList<>();

	public void broadcast(String message) {
		for (GamePlayer player : players) {
			player.send(message);
		}
	}

	public boolean isPlaying(Player player) {
		for (GamePlayer p : players) {
			if (p.getPlayer().equals(player)) {
				return true;
			}

		}
		return false;
	}

	public List<GamePlayer> getPlayers() {
		return players;
	}

	public List<Player> getPlaying() {
		List<Player> list = new ArrayList<>();
		for (GamePlayer player : players) {
			list.add(player.getPlayer());
		}
		return list;
	}

	public void setPlayers(List<GamePlayer> players) {
		this.players = players;
	}

	public void disable() {
		setEnabled(false);
	}

	public void enable() {
		setEnabled(true);
	}

	public void preStart() {
		setState(MinigameState.STARTING);
		System.out.println(getMap().getTimeIntoStart());
		setTime(getMap().getTimeIntoStart());
	}

	public void startForced() {
		setTime(getMap().getTimeOnForceTimer());
	}

	public void start() {
		setTime(getMap().getTimeOnStartTimer());
		setState(MinigameState.PLAYING);
	}

	public boolean checkEnd() {
		return getTime() == getMap().getTimeIntoGameOver();
	}

	public boolean checkWinner() {
		return players.size() == 1;
	}

	public boolean checkTeamWinner() {
		return teams.stream().filter(team -> team.getPlayers(GamePlayerState.NORMAL).size() > 0).count() == 1;
	}

	public GamePlayer getWinner() {
		return players.get(0);
	}

	public GameTeam getTeamWinner() {
		return teams.stream().filter(team -> team.getPlayers(GamePlayerState.NORMAL).size() > 0).findFirst().get();
	}

	public void preGame() {
		setTime(getMap().getTimeWithoutPvP());
		setState(MinigameState.EQUIPPING);
	}

	public void preEnd() {
		setTime(getMap().getTimeOnRestartTimer());
		setState(MinigameState.ENDING);
	}

	public void preRestart() {
		setTime(getMap().getTimeOnRestartTimer());
		setState(MinigameState.RESTARTING);
	}

	public void leaveAll() {
		Iterator<GamePlayer> it = players.iterator();
		if (it.hasNext()) {
			GamePlayer player = it.next();
			leave(player);
		}
	}

	public void leave(GamePlayer player) {
		player.getGame().getPlayers().remove(player);
		player.setGame(null);
	}

	public void restart() {
		setState(MinigameState.RESTARTING);

	}

	public List<Player> getPlayers(GamePlayerState state) {
		List<Player> list = new ArrayList<>();
		for (GamePlayer player : players) {
			if (player.getState() == state)
				list.add(player.getPlayer());
		}
		return list;
	}

	public Game(Minigame minigame, GameMap map) {
		this.minigame = minigame;
		this.id = minigame.getRooms().size() + 1;
		this.minigame.getRooms().put(id, this);
		this.map = map;
		this.enabled = true;
		this.time = map.getTimeIntoStart();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MinigameState getState() {
		return state;
	}

	public void setState(MinigameState state) {
		this.state = state;
	}

	public GameMap getMap() {
		return map;
	}

	public void setMap(GameMap map) {
		this.map = map;
	}

	public Minigame getMinigame() {
		return minigame;
	}

	public void setMinigame(Minigame minigame) {
		this.minigame = minigame;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<GameTeam> getTeams() {
		return teams;
	}

	public void setTeams(List<GameTeam> teams) {
		this.teams = teams;
	}

	public boolean isState(MinigameState state) {
		return this.state == state;
	}

	public void emptyTeams() {
		for (GameTeam team : teams) {
			team.leaveAll();
		}
	}

}