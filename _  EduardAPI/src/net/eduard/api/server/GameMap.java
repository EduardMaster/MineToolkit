package net.eduard.api.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

import net.eduard.api.setup.StorageAPI.Storable;

/**
 * Mapa da Sala
 * @author Eduard-PC
 *
 */
public class GameMap implements Storable{
	
	private String name;
	private World world;
	private int minPlayersAmount=2;
	private int maxPlayersAmount=20;
	private int neededPlayersAmount=16;
	private Location lobby;
	private Location spawn;
	private Arena feast;
	private Arena map;
	private List<Location> spawns = new ArrayList<>();
	private List<Arena> minifeasts= new ArrayList<>();
	private Location feastPosition;
	private Location highPosition;
	private Location lowPosition;
	private int timeIntoStart=60;
	private int timeIntoRestart=20;
	private int timeIntoGameOver=15*60;
	private int timeWithoutPvP=2*60;
	private int timeOnRestartTimer=40;
	private int timeOnForceTimer=10;
	
	public int getTimeIntoStart() {
		return timeIntoStart;
	}
	public GameMap() {
		// TODO Auto-generated constructor stub
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

	
	
	public GameMap(String name) {
		super();
		this.name = name;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
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

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	public Location getSpawn() {
		return spawn;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public Arena getFeast() {
		return feast;
	}

	public void setFeast(Arena feast) {
		this.feast = feast;
	}

	public List<Arena> getMinifeasts() {
		return minifeasts;
	}

	public void setMinifeasts(List<Arena> minifeasts) {
		this.minifeasts = minifeasts;
	}

	public Location getFeastPosition() {
		return feastPosition;
	}

	public void setFeastPosition(Location feastPosition) {
		this.feastPosition = feastPosition;
	}

	public Location getHighPosition() {
		return highPosition;
	}

	public void setHighPosition(Location highPosition) {
		this.highPosition = highPosition;
	}

	public Location getLowPosition() {
		return lowPosition;
	}

	public void setLowPosition(Location lowPosition) {
		this.lowPosition = lowPosition;
	}

	public Arena getMap() {
		return map;
	}

	public void setMap(Arena map) {
		this.map = map;
	}

	public List<Location> getSpawns() {
		return spawns;
	}

	public void setSpawns(List<Location> spawns) {
		this.spawns = spawns;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}


}
