package net.eduard.api.server;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.eduard.api.API;
import net.eduard.api.event.MinigameEvent;
import net.eduard.api.manager.EventsManager;

public abstract class Minigame extends EventsManager{

	private String name;
	private Location lobby;
	private Map<String, GameMap> maps = new HashMap<>();
	private transient Map<Integer, Game> rooms = new HashMap<>();
	private transient Map<Player, Game> playing = new HashMap<>();
	private transient BukkitTask minigameRunnning;
	public Minigame() {
	}
	public Minigame(String name) {
		setName(name);
	}
	public Minigame(String name, Plugin plugin) {
		setName(name);
		enable(plugin);
	}
	public void disable() {
		if (isRunning()) {
			minigameRunnning.cancel();
			minigameRunnning = null;
		}
	}
	public boolean isRunning() {
		return minigameRunnning != null;
	}
	public void enable(Plugin plugin) {
		
		minigameRunnning = new BukkitRunnable() {

			@Override
			public void run() {
				for (Game game : rooms.values()) {
					MinigameEvent event = new MinigameEvent(Minigame.this, game);
					if (event.isCancelled()) {
						continue;
					}
					event(game);
				}

			}
		}.runTaskTimer(plugin, 20, 20);
	}
	public abstract void event(Game room);
	public void broadcast(Object... text) {
		API.send(playing.keySet(), text);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, GameMap> getMaps() {
		return maps;
	}

	public void setMaps(Map<String, GameMap> maps) {
		this.maps = maps;
	}

	public Map<Integer, Game> getRooms() {
		return rooms;
	}

	public void setRooms(Map<Integer, Game> rooms) {
		this.rooms = rooms;
	}

	public Map<Player, Game> getPlaying() {
		return playing;
	}

	public void setPlaying(Map<Player, Game> playing) {
		this.playing = playing;
	}

	


	public BukkitTask getMinigameRunnning() {
		return minigameRunnning;
	}

	public void setMinigameRunnning(BukkitTask minigameRunnning) {
		this.minigameRunnning = minigameRunnning;
	}

	public Location getLobby() {
		return lobby;
	}
	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	
}
