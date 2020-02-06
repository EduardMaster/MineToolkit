package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.storage.StorageAttributes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.storage.Storable;

/**
 * Mapa da Sala
 * 
 * @author Eduard-PC
 *
 */
@StorageAttributes(indentificate = true)
public class MinigameMap implements Storable, Copyable {

	private String name;
	private String displayName;
	private String worldName;
	private int teamSize = 1;
	private int minPlayersAmount = 2;
	private int maxPlayersAmount = 20;
	private int neededPlayersAmount = 16;
	private int maxRounds=3;
	private Location spawn, lobby;
	private Map<String, Location> locations = new HashMap<>();
	private List<Schematic> bases = new ArrayList<>();
	private List<Location> spawns = new ArrayList<>();
	private Schematic map, feast;
	private Location feastLocation;

	public boolean isSolo() {
		return teamSize == 1;
	}

	public MinigameMap() {

		// TODO Auto-generated constructor stub
	}

	public MinigameMap copy() {
		return copy(this);

	}

	public MinigameMap(String name) {
		this.name = name;
		this.displayName = name;
	}

	public MinigameMap(Minigame minigame, String name) {
		this(name);
		minigame.getMaps().add(this);
	}

	@Override
	public Object restore(Map<String, Object> map) {
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	public MinigameMap world(World world) {
		this.worldName = world.getName();
		for (Location spawn : this.spawns) {
			spawn.setWorld(world);
		}
		if (hasSpawn()) {
			spawn.setWorld(world);
		}
		if (hasLobby()) {
			lobby.setWorld(world);
		}
		for (Location loc : locations.values()) {
			loc.setWorld(world);
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
		World mundo = Bukkit.getWorld(worldName);
		if (mundo == null) {
			mundo = Mine.loadWorld(worldName);

		}
		return mundo;
	}

	public MinigameMap fixWorld() {
		return world(getWorld());
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

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

}