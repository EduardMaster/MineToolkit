package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.eduard.api.setup.StorageAPI.Storable;
import net.eduard.api.setup.game.Schematic;

/**
 * Mapa da Sala
 * 
 * @author Eduard-PC
 *
 */
public class GameMap implements Storable {

	private String name;
	private World world = Bukkit.getWorld("world");
	private int timeIntoStart = 60;
	private int timeIntoRestart = 20;
	private int timeIntoGameOver = 15 * 60;
	private int timeWithoutPvP = 2 * 60;
	private int timeOnStartTimer = 0;
	private int timeOnRestartTimer = 40;
	private int timeOnForceTimer = 10;
	private int minPlayersAmount = 2;
	private int maxPlayersAmount = 20;
	private int neededPlayersAmount = 16;
	private int maxTeamSize = 5;
	private Location spawn;
	private Location lobby;
	private Location feast;
	private Schematic map;
	private List<Location> spawns = new ArrayList<>();
	private Map<String,  Location> locations = new HashMap<>();
	private List<Schematic> bases = new ArrayList<>();
	public GameMap() {
		// TODO Auto-generated constructor stub
	}
	public GameMap(Minigame minigame, String name) {
		this.name = name;
		minigame.getMaps().put(name.toLowerCase(), this);
	}
	public GameMap(String name) {
		this.name = name;
	}
	@Override
	public Object restore(Map<String, Object> map) {
//		System.out.println("Restaurando dados GameMap");
//		System.out.println("Valor "+timeIntoStart);
		return null;
	}
	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}
	public void world(World world) {
		this.world = world;
//		for (Schematic base : this.bases) {
//			base.world(world);
//		}
		for (Location spawn : this.spawns) {
			spawn.setWorld(world);
		}
		if (hasLobby()) {
			lobby.setWorld(world);
		}
		if (hasFeast()) {
			feast.setWorld(world);
		}
		for (Entry<String, Location> entry : this.locations.entrySet()) {
			entry.getValue().setWorld(world);
		}
		if (hasSchematic()) {
//			this.map.world(world);
		}
			
	}
	public boolean hasFeast() {
		// TODO Auto-generated method stub
		return feast != null;
	}
	public boolean hasLobby() {
		return lobby!=null;
	}
	public boolean hasBases() {
		return !this.bases.isEmpty();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
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
	public int getMinPlayersAmount() {
		return minPlayersAmount;
	}
	public void setMinPlayersAmount(int minPlayersAmount) {
		this.minPlayersAmount = minPlayersAmount;
	}
	public int getMaxPlayersAmount() {
		return maxPlayersAmount;
	}
	public void setMaxPlayersAmount(int maxPlayersAmount) {
		this.maxPlayersAmount = maxPlayersAmount;
	}
	public int getNeededPlayersAmount() {
		return neededPlayersAmount;
	}
	public void setNeededPlayersAmount(int neededPlayersAmount) {
		this.neededPlayersAmount = neededPlayersAmount;
	}
	public int getMaxTeamSize() {
		return maxTeamSize;
	}
	public void setMaxTeamSize(int maxTeamSize) {
		this.maxTeamSize = maxTeamSize;
	}
	public Location getSpawn() {
		return spawn;
	}
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	public Location getLobby() {
		return lobby;
	}
	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}
	public Location getFeast() {
		return feast;
	}
	public void setFeast(Location feast) {
		this.feast = feast;
	}
	public Schematic getMap() {
		return map;
	}
	public void setMap(Schematic map) {
		this.map = map;
	}
	public List<Location> getSpawns() {
		return spawns;
	}
	public void setSpawns(List<Location> spawns) {
		this.spawns = spawns;
	}
	public List<Schematic> getBases() {
		return bases;
	}
	public void setBases(List<Schematic> bases) {
		this.bases = bases;
	}
	public boolean hasSpawn() {
		return spawn != null;
	}
	public boolean hasSpawns() {
		return !spawns.isEmpty();
	}
	public Map<String,  Location> getLocations() {
		return locations;
	}
	public void setLocations(Map<String,  Location> locations) {
		this.locations = locations;
	}
	public boolean hasSchematic() {
		return this.map != null;
	}
	public int getTimeOnStartTimer() {
		return timeOnStartTimer;
	}
	public void setTimeOnStartTimer(int timeOnStartTimer) {
		this.timeOnStartTimer = timeOnStartTimer;
	}
	
	

}