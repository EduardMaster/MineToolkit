package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.DisplayBoard;
import net.eduard.api.lib.game.Kit;
import net.eduard.api.lib.game.MinigameChest;
import net.eduard.api.lib.manager.TimeManager;

/**
 * Representa um Jogo <br>
 * MinigameSetup 1.0
 * 
 * @version 2.0
 * @since EduardAPI 2.0
 * @author Eduard
 *
 */
public class Minigame extends TimeManager{

	private String name;
	private boolean enabled = true;
	private boolean bungeecord = true;
	private String bungeeLobby = "Lobby";
	private Location lobby;
	private int timeIntoStart = 60;
	private int timeIntoRestart = 20;
	private int timeIntoGameOver = 15 * 60;
	private int timeIntoPlay = 2 * 60;
	
	
	private int timeOnStartTimer = 0;
	private int timeOnRestartTimer = 40;
	private int timeOnForceTimer = 10;
	private int timeOnStartingToBroadcast=15;
	private int timeOnEquipingToBroadcast=1;
	private transient MinigameMap setting;
	private transient List<MinigamePlayer> players = new ArrayList<>();
	private DisplayBoard scoreboardStarting;
	private DisplayBoard scoreboardLobby;
	private DisplayBoard scoreboardPlaying;
	private MinigameChest chests = new MinigameChest();
	private MinigameChest chestsFeast = new MinigameChest();
	private MinigameChest chestMiniFeast = new MinigameChest();
	private List<Kit> kits = new ArrayList<>();
	private List<MinigameMap> maps = new ArrayList<>();
	private List<MinigameRoom> rooms = new ArrayList<>();
	public Minigame() {
	}
	public int getTimeIntoPlay() {
		return timeIntoPlay;
	}

	public void setTimeIntoPlay(int timeIntoPlay) {
		this.timeIntoPlay = timeIntoPlay;
	}

	public Minigame(String name) {
		setName(name);

	}
	public MinigameMap createMap(String nome) {
		MinigameMap map = new MinigameMap(nome);
		getMaps().add(map);
		return map;

	}
	public void event(MinigameRoom room) {
	}

	public MinigameMap getMap(String name) {
		for (MinigameMap map : getMaps()) {
			if (map.getName().equalsIgnoreCase(name)) {
				return map;
			}
		}
		return null;
	}
	public void removeMap(MinigameMap map) {
		getMaps().remove(map);
	}

	public boolean hasMap(String name) {
		for (MinigameMap map : getMaps()) {
			if (map.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	public MinigameRoom uniqueGame() {
		return new MinigameRoom(this, new MinigameMap(this, getName()));
	}

	public Minigame(String name, Plugin plugin) {
		this(name);
		setPlugin(plugin);
	}


	public void broadcast(String message) {
		for (Player player : getPlaying()) {
			player.sendMessage(Mine.getReplacers(message, player));
		}
	}


	public String getBungeeLobby() {
		return bungeeLobby;
	}

	public MinigameRoom getGame() {
		return getRooms().iterator().next();
	}

	public MinigameRoom getRoom(int id) {
		for (MinigameRoom room : rooms) {
			if (room.getId() == id) {
				return room;
			}
		}
		return null;
	}

	public boolean hasRoom(int id) {
		return getRoom(id) != null;
	}

	public MinigameRoom createRoom(MinigameMap map, int id) {
		MinigameRoom room = new MinigameRoom(this, map);
		room.setId(id);
		return room;

	}

	public List<MinigameRoom> getRooms() {
		return rooms;
	}

	public void setRooms(List<MinigameRoom> rooms) {
		this.rooms = rooms;
	}

	public List<MinigamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<MinigamePlayer> players) {
		this.players = players;
	}

	public MinigameRoom getGame(Player player) {
		return getPlayer(player).getGame();
	}

	public MinigameRoom getGame(String name) {
		for (MinigameRoom room : rooms) {
			if (room.getMap().getName().equalsIgnoreCase(name)) {
				return room;
			}
		}
		return null;
	}

	public Location getLobby() {
		return lobby;
	}

	public MinigameMap getMap() {
		return getMap(getName());
	}

	public String getName() {
		return name;
	}

	public MinigamePlayer getPlayer(Player player) {
		MinigamePlayer miniplayer;
		for (MinigamePlayer p : players) {
			if (p.getPlayer().equals(player)) {
				return p;
			}
		}
		miniplayer = new MinigamePlayer(player);
		players.add(miniplayer);

		return miniplayer;
	}

	public List<Player> getPlaying() {
		List<Player> list = new ArrayList<>();
		for (MinigamePlayer player : players) {
			list.add(player.getPlayer());
		}
		return list;
	}

	public boolean hasLobby() {
		return lobby != null;
	}

	public boolean isAdmin(Player player) {
		return getPlayer(player).isState(MinigamePlayerState.ADMIN);

	}

	public boolean isBungeecord() {
		return bungeecord;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isPlayer(Player player) {
		return getPlayer(player).isState(MinigamePlayerState.NORMAL);

	}

	public boolean isPlaying(Player player) {
		return getPlayer(player).isPlaying();
	}

	public boolean isSpectator(Player player) {
		return getPlayer(player).isState(MinigamePlayerState.SPECTATOR);

	}

	public boolean isState(MinigameState state) {
		return getGame().isState(state);
	}


	public void joinPlayer(MinigameRoom game, Player player) {
		MinigamePlayer p = getPlayer(player);
		p.join(game);

	}

	public void joinPlayer(MinigameTeam team, Player player) {
		MinigamePlayer p = getPlayer(player);
		p.join(team);
	}

	public void leavePlayer(Player player) {
		MinigamePlayer p = getPlayer(player);
		if (p.isPlaying()) {
			p.getGame().leave(p);
		}

		if (p.hasTeam()) {
			p.getTeam().leave(p);
		}

	}

	public void remove(Player player) {
		players.remove(getPlayer(player));
	}

	public void removeGame(MinigameRoom game) {
		this.rooms.remove(game);
	}

	public void removeGame(int id) {
		MinigameRoom game = getRoom(id);
		this.rooms.remove(game);
	}

	public Object restore(Map<String, Object> map) {

		return null;
	}

	public void run() {
		if (!enabled)
			return;
		for (MinigameRoom room : rooms) {
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

	public void setName(String name) {
		this.name = name;
	}

	public MinigameMap getSetting() {
		return setting;
	}

	public boolean isSetting() {
		return setting != null;
	}

	public void setSetting(MinigameMap setting) {
		this.setting = setting;
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
		return timeIntoPlay;
	}

	public void setTimeWithoutPvP(int timeWithoutPvP) {
		this.timeIntoPlay = timeWithoutPvP;
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

	public DisplayBoard getScoreboardLobby() {
		return scoreboardLobby;
	}

	public void setScoreboardLobby(DisplayBoard scoreboardLobby) {
		this.scoreboardLobby = scoreboardLobby;
	}

	public DisplayBoard getScoreboardPlaying() {
		return scoreboardPlaying;
	}

	public void setScoreboardPlaying(DisplayBoard scoreboardPlaying) {
		this.scoreboardPlaying = scoreboardPlaying;
	}

	public MinigameChest getChests() {
		return chests;
	}

	public void setChests(MinigameChest chests) {
		this.chests = chests;
	}

	public MinigameChest getChestsFeast() {
		return chestsFeast;
	}

	public void setChestsFeast(MinigameChest chestsFeast) {
		this.chestsFeast = chestsFeast;
	}

	public MinigameChest getChestMiniFeast() {
		return chestMiniFeast;
	}

	public void setChestMiniFeast(MinigameChest chestMiniFeast) {
		this.chestMiniFeast = chestMiniFeast;
	}

	public List<Kit> getKits() {
		return kits;
	}

	public void setKits(List<Kit> kits) {
		this.kits = kits;
	}

	public List<MinigameMap> getMaps() {
		return maps;
	}

	public void setMaps(List<MinigameMap> maps) {
		this.maps = maps;
	}

	public DisplayBoard getScoreboardStarting() {
		return scoreboardStarting;
	}

	public void setScoreboardStarting(DisplayBoard scoreboardStarting) {
		this.scoreboardStarting = scoreboardStarting;
	}
	public int getTimeOnStartingToBroadcast() {
		return timeOnStartingToBroadcast;
	}
	public void setTimeOnStartingToBroadcast(int timeOnStartingToBroadcast) {
		this.timeOnStartingToBroadcast = timeOnStartingToBroadcast;
	}
	public int getTimeOnEquipingToBroadcast() {
		return timeOnEquipingToBroadcast;
	}
	public void setTimeOnEquipingToBroadcast(int timeOnEquipingToBroadcast) {
		this.timeOnEquipingToBroadcast = timeOnEquipingToBroadcast;
	}

}