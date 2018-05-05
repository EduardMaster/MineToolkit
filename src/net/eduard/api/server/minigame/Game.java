package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

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
	private GameMap map;
	private MinigameMode mode;
	private MinigameState state = MinigameState.STARTING;
	private List<GamePlayer> players = new ArrayList<>();
	private List<Chest> chests = new ArrayList<>();
	private List<Chest> chestsFeast = new ArrayList<>();
	private List<GameTeam> teams = new ArrayList<>();

	public List<Chest> getChests() {
		return chests;
	}

	public void setChests(List<Chest> chests) {
		this.chests = chests;
	}

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

	public boolean checkEnd() {
		return getTime() == getMinigame().getTimeIntoGameOver();
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

	public boolean checkForceStart() {
		return getPlayers().size() >= map.getNeededPlayersAmount() && getMinigame().getTimeOnForceTimer() > time;
	}

	public void doPreStart() {
		setState(MinigameState.STARTING);
		setTime(getMinigame().getTimeIntoStart());
	}

	public void doStartForced() {
		setTime(getMinigame().getTimeOnForceTimer());
	}

	public void doStart() {
		setTime(getMinigame().getTimeOnStartTimer());
		setState(MinigameState.PLAYING);
	}

	public void doPreGame() {
		setTime(getMinigame().getTimeWithoutPvP());
		setState(MinigameState.EQUIPPING);
	}

	public void doPreEnd() {
		setTime(getMinigame().getTimeOnRestartTimer());
		setState(MinigameState.ENDING);
	}

	public void doPreRestart() {
		setTime(getMinigame().getTimeOnRestartTimer());
		setState(MinigameState.RESTARTING);
	}

	public void doRestart() {
		setState(MinigameState.RESTARTING);

	}

	public void leave(GamePlayer player) {
		player.getGame().getPlayers().remove(player);
		player.setGame(null);
	}

	public void leaveAll() {
		Iterator<GamePlayer> it = players.iterator();
		if (it.hasNext()) {
			GamePlayer player = it.next();
			leave(player);
		}
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
		this.time = getMinigame().getTimeIntoStart();
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

	public MinigameMode getMode() {
		return mode;
	}

	public void setMode(MinigameMode mode) {
		this.mode = mode;
	}

	public List<Chest> getChestsFeast() {
		return chestsFeast;
	}

	public void setChestsFeast(List<Chest> chestsFeast) {
		this.chestsFeast = chestsFeast;
	}

	public void join(GamePlayer player) {
		players.add(player);
	}

}