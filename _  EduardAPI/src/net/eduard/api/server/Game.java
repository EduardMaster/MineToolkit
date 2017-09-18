package net.eduard.api.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import net.eduard.api.API;
/**
 * Sala do Minigame
 * @author Eduard-PC
 *
 */
public class Game {

	private int id;
	private int time;
	private Minigame minigame;
	private GameMap map;
	private GameState state = GameState.STARTING;
	private World world = Bukkit.getWorlds().get(0);
	private List<Player> players = new ArrayList<>();
	private List<Player> spectators = new ArrayList<>();
	private List<Player> admins = new ArrayList<>();
	private List<Player> winners = new ArrayList<>();
	private List<GameTeam> teams= new ArrayList<>();
	
	
	public Game(Minigame minigame,GameMap map) {
		this.minigame = minigame;
		this.id = minigame.getRooms().size()+1;
		this.minigame.getRooms().put(id, this);
		this.map = map;
		this.time = map.getTimeIntoStart();
	}
	public void sendToPlayers(Object...text){
		API.send(players, text);
	}
	public void sendToSpecs(Object...text){
		API.send(spectators, text);
	}
	public void sendToAdmins(Object...text){
		API.send(admins, text);
	}
	public void broadcast(Object...text){
		sendToAdmins(text);
		sendToPlayers(text);
		sendToSpecs(text);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
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


	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public List<Player> getSpectators() {
		return spectators;
	}

	public void setSpectators(List<Player> spectators) {
		this.spectators = spectators;
	}

	public List<Player> getAdmins() {
		return admins;
	}

	public void setAdmins(List<Player> admins) {
		this.admins = admins;
	}


	public World getWorld() {
		return world;
	}


	public void setWorld(World world) {
		this.world = world;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public List<Player> getWinners() {
		return winners;
	}
	public void setWinners(List<Player> winners) {
		this.winners = winners;
	}
	public List<GameTeam> getTeams() {
		return teams;
	}
	public void setTeams(List<GameTeam> teams) {
		this.teams = teams;
	}

}
