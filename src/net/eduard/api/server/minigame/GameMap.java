package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

import net.eduard.api.lib.Copyable;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.storage.Storable;

/**
 * Mapa da Sala
 * 
 * @author Eduard-PC
 *
 */
public class GameMap implements Storable ,Copyable{

	private String name;
	private int teamSize;
	private int minPlayersAmount = 2;
	private int maxPlayersAmount = 20;
	private int neededPlayersAmount = 16;
	private Location spawn, lobby;
	private Map<String, Location> locations = new HashMap<>();
	private List<Schematic> bases = new ArrayList<>();
	private List<Location> spawns = new ArrayList<>();
	private Schematic map, feast;

	private Location feastLocation;

	public GameMap() {

		// TODO Auto-generated constructor stub
	}

	public GameMap copy() {
		return copy(this);

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
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	public GameMap world(World world) {

		for (Location spawn : this.spawns) {
			spawn.setWorld(world);
		}
		if (hasSpawn()) {
			spawn.setWorld(world);
		}
		if (hasLobby()) {
			lobby.setWorld(world);
		}

		return this;
	}

	public void paste(Location relative) {
		world(relative.getWorld());

		Location dif = relative.clone().subtract(map.getRelative());
		if (hasSpawn()) {
			this.spawn.add(dif);
		}
		if (hasLobby()) {
			this.lobby.add(dif);
		}
		for (Location spawn : spawns) {
			spawn.add(dif);
		}
		map.paste(relative, true);

	}

	public Schematic getFeast() {
		return feast;
	}

	public void setFeast(Schematic feast) {
		this.feast = feast;
	}

	public boolean hasFeast() {
		// TODO Auto-generated method stub
		return feast != null;
	}

	public boolean hasLobby() {
		return lobby != null;
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

	public Schematic getMap() {
		return map == null ? (map = new Schematic()) : map;
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

	public boolean hasSchematic() {
		return this.map != null;
	}

	public World getWorld() {
		return getSpawn().getWorld();
	}


	
	public Location getFeastLocation() {
		return feastLocation;
	}

	public void setFeastLocation(Location feastLocation) {
		this.feastLocation = feastLocation;
	}


	public int getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(int teamSize) {
		this.teamSize = teamSize;
	}

	public Map<String, Location> getLocations() {
		return locations;
	}

	public void setLocations(Map<String, Location> locations) {
		this.locations = locations;
	}

}