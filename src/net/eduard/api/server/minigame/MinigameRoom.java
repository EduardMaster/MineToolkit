package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.storage.StorageAttributes;
import net.eduard.api.lib.storage.Storable;

/**
 * Sala do Minigame
 * 
 * @author Eduard-PC
 *
 */
public class MinigameRoom implements Storable {

	@StorageAttributes(reference=true)
	private Minigame minigame;
	@StorageAttributes(reference=true)
	private MinigameMap map;
	private MinigameMode mode = MinigameMode.NORMAL;
	private int id;
	private int time;
	private boolean enabled;
	private transient MinigameState state = MinigameState.STARTING;
	private transient MinigameMap mapUsed;

	private transient List<MinigamePlayer> players = new ArrayList<>();
	private transient List<MinigameTeam> teams = new ArrayList<>();

	public MinigameRoom() {
	}

	public void broadcast(String message) {
		for (MinigamePlayer player : players) {
			player.send(
					message.replace("$time", Mine.getTime(time)).replace("$max", "" + getMap().getMaxPlayersAmount())
							.replace("$players", "" + getPlayers().size()));
		}
	}

	public boolean isPlaying(Player player) {
		return players.stream().filter(p -> p.getPlayer().equals(player)).findFirst().isPresent();
	}

	public List<Player> getPlayersOnline() {

		return players.stream().map(p -> p.getPlayer()).collect(Collectors.toList());
	}

	public List<MinigameTeam> getTeams(MinigamePlayerState state) {
		return teams.stream().filter(t -> t.getPlayers(state).size() > 0).collect(Collectors.toList());
	}

	public MinigameTeam getTeamWinner() {
		return teams.stream().filter(t -> t.getPlayers(MinigamePlayerState.NORMAL).size() > 0).findFirst().get();
	}

	public List<Player> getPlayersOnline(MinigamePlayerState state) {

		return players.stream().filter(p -> p.getState() == state).map(p -> p.getPlayer()).collect(Collectors.toList());
	}

	public List<MinigamePlayer> getPlayers(MinigamePlayerState state) {
		return players.stream().filter(p -> p.getState() == state).collect(Collectors.toList());
	}

	public List<MinigamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<MinigamePlayer> players) {
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
		return getPlayers(MinigamePlayerState.NORMAL).size() == 1;
	}

	public boolean checkTeamWinner() {
		return teams.stream().filter(team -> team.getPlayers(MinigamePlayerState.NORMAL).size() > 0).count() == 1;
	}

	public MinigamePlayer getWinner() {
		return getPlayers(MinigamePlayerState.NORMAL).get(0);
	}


	public boolean checkForceStart() {
		return getPlayers().size() >= map.getNeededPlayersAmount() && time > getMinigame().getTimeOnForceTimer();
	}

	public void forceGameStart() {
		setTime(getMinigame().getTimeOnForceTimer());
	}

	public int advance() {
		return ++time;
	}

	public int decrease() {
		return --time;
	}

	public void startGame() {
		setTime(getMinigame().getTimeOnStartTimer());
		setState(MinigameState.PLAYING);
	}

	public void startPreGame() {
		setTime(getMinigame().getTimeIntoPlay());
		setState(MinigameState.EQUIPPING);
	}

	public void ending() {
		setTime(getMinigame().getTimeOnRestartTimer());
		setState(MinigameState.ENDING);
	}

	public void restarting() {
		setState(MinigameState.RESTARTING);
		setTime(getMinigame().getTimeIntoRestart());
	}

	public void restart() {
		setState(MinigameState.STARTING);
		setTime(getMinigame().getTimeIntoStart());
	}

	public void initMinigame() {
		setState(MinigameState.STARTING);
		setTime(getMinigame().getTimeIntoStart());
	}

	public void leave(MinigamePlayer player) {
		player.getGame().getPlayers().remove(player);
		player.setGame(null);
	}

	public void leaveAll() {
		Iterator<MinigamePlayer> it = players.iterator();
		if (it.hasNext()) {
			MinigamePlayer player = it.next();
			leave(player);
		}
	}

	public MinigameRoom(Minigame minigame, MinigameMap map) {
		this.minigame = minigame;
		this.id = minigame.getRooms().size() + 1;
		this.minigame.getRooms().add(this);
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

	public MinigameMap getMap() {
		return map;
	}

	public void setMap(MinigameMap map) {
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

	public List<MinigameTeam> getTeams() {
		return teams;
	}

	public void setTeams(List<MinigameTeam> teams) {
		this.teams = teams;
	}

	public boolean isState(MinigameState state) {
		return this.state == state;
	}

	public void emptyTeams() {
		for (MinigameTeam team : teams) {
			team.leaveAll();
		}
	}

	public void join(MinigamePlayer player) {
		player.join(this);
	}

	public MinigameMap getMapUsed() {
		return mapUsed;
	}

	public void setMapUsed(MinigameMap mapUsed) {
		this.mapUsed = mapUsed;
	}

	public MinigameMode getMode() {
		return mode;
	}

	public void setMode(MinigameMode mode) {
		this.mode = mode;
	}

	public boolean hasSpace() {
		return getPlayers().size() < getMap().getMaxPlayersAmount();
	}

}