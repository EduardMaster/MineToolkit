package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Kit;
import net.eduard.api.setup.manager.TimeManager;

public abstract class Minigame extends TimeManager {

	private String name;
	private boolean enabled = true;
	private boolean bungeecord = true;
	private String bungeeLobby = "Lobby";
	private Location lobby;
	private Map<String, GameMap> maps = new HashMap<>();
	private List<Kit> kits = new ArrayList<>();
	private int timeIntoStart = 60;
	private int timeIntoRestart = 20;
	private int timeIntoGameOver = 15 * 60;
	private int timeWithoutPvP = 2 * 60;
	private int timeOnStartTimer = 0;
	private int timeOnRestartTimer = 40;
	private int timeOnForceTimer = 10;
	private List<MinigameMode> modes = new ArrayList<>();
	public List<MinigameMode> getModes() {
		return modes;
	}

	public void setModes(List<MinigameMode> modes) {
		this.modes = modes;
	}

	public MinigameMode getMode(String name) {
		for (MinigameMode mode : modes) {
			if (mode.getName().equalsIgnoreCase(name)) {
				return mode;
			}
		}
		return null;
	}

	private transient GameMap setting;
	private transient Map<Integer, Game> rooms = new HashMap<>();
	private transient Map<Player, GamePlayer> players = new HashMap<>();



	public Minigame() {
	}


	public Minigame(String name) {
		setName(name);

	}

	public Game uniqueGame() {
		return new Game(this, new GameMap(this, getName()));
	}

	public Minigame(String name, Plugin plugin) {
		this(name);
		setPlugin(plugin);
	}

	public GameMap createMap(String nome) {
		return new GameMap(this, nome);
	}

	public void broadcast(String message) {
		for (Player player : players.keySet()) {
			player.sendMessage(Mine.getReplacers(message, player));
		}
	}

	public abstract void event(Game room);

	public boolean existsMap(String name) {
		return this.maps.containsKey(name.toLowerCase());
	}

	public String getBungeeLobby() {
		return bungeeLobby;
	}

	public Game getGame() {
		return getRooms().get(1);
	}

	public Game getGame(Player player) {
		return getPlayer(player).getGame();
	}

	public Game getGame(String name) {
		for (Game room : rooms.values()) {
			if (room.getMap().getName().equalsIgnoreCase(name)) {
				return room;
			}
		}
		return null;
	}

	public Location getLobby() {
		return lobby;
	}

	public GameMap getMap() {
		return getMaps().get(getName().toLowerCase());
	}

	public GameMap getMap(String name) {
		return maps.get(name.toLowerCase());
	}

	public Map<String, GameMap> getMaps() {
		return maps;
	}

	public String getName() {
		return name;
	}

	public GamePlayer getPlayer(Player player) {
		GamePlayer gamePlayer = players.get(player);
		if (gamePlayer == null) {
			gamePlayer = new GamePlayer(player);
			players.put(player, gamePlayer);
		}

		return gamePlayer;
	}

	public Map<Player, GamePlayer> getPlayers() {
		return players;
	}

	public List<Player> getPlaying() {
		List<Player> list = new ArrayList<>();
		for (Player player : players.keySet()) {
			list.add(player);
		}
		return list;
	}

	public Map<Integer, Game> getRooms() {
		return rooms;
	}

	public boolean hasLobby() {
		return lobby != null;
	}

	public boolean hasMap(String name) {
		return maps.containsKey(name.toLowerCase());
	}

	public boolean isAdmin(Player player) {
		return getPlayer(player).isState(GamePlayerState.ADMIN);

	}

	public boolean isBungeecord() {
		return bungeecord;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isPlayer(Player player) {
		return getPlayer(player).isState(GamePlayerState.NORMAL);

	}

	public boolean isPlaying(Player player) {
		return getPlayer(player).isPlaying();
	}

	public boolean isSpectator(Player player) {
		return getPlayer(player).isState(GamePlayerState.SPECTATOR);

	}

	public boolean isState(MinigameState state) {
		return getGame().isState(state);
	}

	public boolean isWinner(Player player) {
		return getPlayer(player).isState(GamePlayerState.WINNER);

	}

	public void joinPlayer(Game game, Player player) {
		GamePlayer p = getPlayer(player);
		p.join(game);

	}

	public void joinPlayer(GameTeam team, Player player) {
		GamePlayer p = getPlayer(player);
		p.join(team);
	}

	public void leavePlayer(Player player) {
		GamePlayer p = getPlayer(player);
		if (p.isPlaying()) {
			p.getGame().leave(p);
		}

		if (p.hasTeam()) {
			p.getTeam().leave(p);
		}

	}

	public void remove(Player player) {
		players.remove(player);
	}

	public void removeGame(Game game) {
		this.rooms.remove(game.getId());
	}

	public void removeGame(int id) {
		this.rooms.remove(id);
	}

	public void removeMap(String name) {
		this.maps.remove(name.toLowerCase());
	}

	public Object restore(Map<String, Object> map) {

		return null;
	}

	public void run() {
		if (!enabled)
			return;
		for (Game room : rooms.values()) {
			if (!room.isEnabled())
				continue;
			event(room);
		}
	}

	public void setBungeecord(boolean bungeecord) {
		this.bungeecord = bungeecord;
	}

	public void setBungeeLobby(String bungeeLobby) {
		this.bungeeLobby = bungeeLobby;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	public void setMaps(Map<String, GameMap> maps) {
		this.maps = maps;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayers(Map<Player, GamePlayer> players) {
		this.players = players;
	}

	public void setRooms(Map<Integer, Game> rooms) {
		this.rooms = rooms;
	}

	public GameMap getSetting() {
		return setting;
	}

	public boolean isSetting() {
		return setting != null;
	}

	public void setSetting(GameMap setting) {
		this.setting = setting;
	}

	public List<Kit> getKits() {
		return kits;
	}

	public void setKits(List<Kit> kits) {
		this.kits = kits;
	}

	public int getTimeIntoStart() {
		return timeIntoStart;
	}

	public void setTimeIntoStart(int timeIntoStart) {
		this.timeIntoStart = timeIntoStart;
	}

	public int getTimeIntoRestart() {
		return timeIntoRestart;
	}

	public void setTimeIntoRestart(int timeIntoRestart) {
		this.timeIntoRestart = timeIntoRestart;
	}

	public int getTimeIntoGameOver() {
		return timeIntoGameOver;
	}

	public void setTimeIntoGameOver(int timeIntoGameOver) {
		this.timeIntoGameOver = timeIntoGameOver;
	}

	public int getTimeWithoutPvP() {
		return timeWithoutPvP;
	}

	public void setTimeWithoutPvP(int timeWithoutPvP) {
		this.timeWithoutPvP = timeWithoutPvP;
	}

	public int getTimeOnStartTimer() {
		return timeOnStartTimer;
	}

	public void setTimeOnStartTimer(int timeOnStartTimer) {
		this.timeOnStartTimer = timeOnStartTimer;
	}

	public int getTimeOnRestartTimer() {
		return timeOnRestartTimer;
	}

	public void setTimeOnRestartTimer(int timeOnRestartTimer) {
		this.timeOnRestartTimer = timeOnRestartTimer;
	}

	public int getTimeOnForceTimer() {
		return timeOnForceTimer;
	}

	public void setTimeOnForceTimer(int timeOnForceTimer) {
		this.timeOnForceTimer = timeOnForceTimer;
	}

}