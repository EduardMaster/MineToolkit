package net.eduard.api.server.minigame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.eduard.api.lib.storage.StorageAttributes;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.DisplayBoard;
import net.eduard.api.lib.game.Kit;
import net.eduard.api.lib.game.MinigameChest;
import net.eduard.api.lib.manager.TimeManager;
import net.eduard.api.lib.modules.BukkitBungeeAPI;

/**
 * Representa um Jogo <br>
 * MinigameSetup 1.0
 * 
 * @version 2.0
 * @since EduardAPI 2.0
 * @author Eduard
 *
 */
@StorageAttributes(indentificate = true)
public class Minigame extends TimeManager {

	private String name = "Minigame";
	private String messagePrefix = "[Minigame] ";
	private boolean enabled = true;
	private boolean bungeecord = true;
	private String bungeeLobby = "Lobby";
	private Location lobby;
	private int maxPlayersPerLobby = 20;
	private int timeIntoStart = 60;
	private int timeIntoRestart = 20;
	private int timeIntoGameOver = 15 * 60;
	private int timeIntoPlay = 2 * 60;

	private int timeOnStartTimer = 0;
	private int timeOnRestartTimer = 40;
	private int timeOnForceTimer = 10;
	private int timeOnStartingToBroadcast = 15;
	private int timeOnEquipingToBroadcast = 1;
	private transient MinigameMap setting;
	private transient List<MinigamePlayer> players = new ArrayList<>();
	private DisplayBoard scoreboardStarting;
	private DisplayBoard scoreboardLobby;
	private DisplayBoard scoreboardPlaying;
	private MinigameChest chests = new MinigameChest();
	private MinigameChest chestsFeast = new MinigameChest();
	private MinigameChest chestMiniFeast = new MinigameChest();
	private List<Kit> kits = new ArrayList<>();
	private List<MinigameLobby> lobbies = new ArrayList<>();
	private List<MinigameMap> maps = new ArrayList<>();
	private List<MinigameRoom> rooms = new ArrayList<>();

	/**
	 * Conecta todos jogadores no servidor Lobby
	 */
	public void connectAllPlayersToLobby() {
		for (MinigamePlayer player : players) {
			BukkitBungeeAPI.connectToServer(player.getPlayer(), bungeeLobby);
		}

	}

	/**
	 * Teleporta todos os jogadores para o Local do Lobby
	 */
	public void teleportAllPlayersToLobby() {
		getPlayers().forEach(p -> p.getPlayer().teleport(getLobby()));
//		for (MinigamePlayer player : getPlayers()) {
//			Player p = player.getPlayer();
//			p.teleport(getLobby());
//		}
	}

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
		setMessagePrefix("§8[§b" + name + "§8] ");
		lobbies.add(new MinigameLobby());
	}

	/**
	 * Cria um Mapa
	 * 
	 * @param nome Nome
	 * @return Mapa Novo
	 */
	public MinigameMap createMap(String nome) {
		MinigameMap map = new MinigameMap(nome);
		getMaps().add(map);
		return map;

	}

	/**
	 * Timer do Minigame define oque acontece a cada segundo que se passa do
	 * Minigame em cada Sala
	 * 
	 * @param room Sala
	 */
	public void event(MinigameRoom room) {
	}

	/**
	 * Pega o mapa existente pelo seu nome
	 * 
	 * @param name Nome
	 * @return Mapa
	 */
	public MinigameMap getMap(String name) {
		for (MinigameMap map : getMaps()) {
			if (map.getName().equalsIgnoreCase(name)) {
				return map;
			}
		}
		return null;
	}

	/**
	 * Remove o mapa da lista de mapas existentes
	 * 
	 * @param map
	 */
	public void removeMap(MinigameMap map) {
		getMaps().remove(map);
	}

	/**
	 * Verifica se existe este Map com este Nome
	 * 
	 * @param name Nome
	 * @return
	 */
	public boolean hasMap(String name) {
		for (MinigameMap map : getMaps()) {
			if (map.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Cria um sala unica e um mapa unico Também usando o Nome do Minigame
	 * 
	 * @return Minigame criado com mapa já configurado
	 */
	public MinigameRoom uniqueGame() {
		return new MinigameRoom(this, new MinigameMap(this, getName()));
	}

	public Minigame(String name, Plugin plugin) {
		this(name);
		setPlugin(plugin);
	}

	/**
	 * Manda mensagem para todos jogadores participando do minigame
	 * 
	 * @param message Mensagem
	 */
	public void broadcast(String message) {
		for (Player player : getPlayersOnline()) {
			player.sendMessage(messagePrefix + Mine.getReplacers(message, player));
		}
	}

	public String getBungeeLobby() {
		return bungeeLobby;
	}

	/**
	 * Pega a primera sala existente do Minigame
	 * 
	 * @return Sala
	 */
	public MinigameRoom getGame() {
//		getRooms().get(0);
		return getRooms().iterator().next();
	}

	/**
	 * Pega a sala pelo seu ID
	 * 
	 * @param id ID
	 * @return Sala
	 */
	public MinigameRoom getRoom(int id) {
		for (MinigameRoom room : rooms) {
			if (room.getId() == id) {
				return room;
			}
		}
		return null;
	}

	/**
	 * Verifica se a sala com este ID existe
	 * 
	 * @param id ID
	 * @return Sala
	 */
	public boolean hasRoom(int id) {
		return getRoom(id) != null;
	}

	/**
	 * Cria uma Sala com este ID para o Mapa expecifico
	 * 
	 * @param map Mapa
	 * @param id  ID
	 * @return Nova Sala
	 */
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

	/**
	 * Pega a sala que o jogador esta jogando
	 * 
	 * @param player Jogador
	 * @return Sala do jogador
	 */
	public MinigameRoom getGame(Player player) {
		return getPlayer(player).getGame();
	}

	/**
	 * Pega a sala com o nome do seu mapa igual a este
	 * 
	 * @param name Nome
	 * @return
	 */
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

	public MinigameLobby getMainLobby() {
		if (lobbies.size() > 0)
			return lobbies.get(0);
		return newLobby(1);
	}

	public MinigameLobby newLobby(int id) {

		MinigameLobby lobby = new MinigameLobby();
		lobby.setId(id);
		lobbies.add(lobby);
		return lobby;

	}

	/**
	 * Pega o mapa referente a sala principal do Minigame
	 * 
	 * @return
	 */
	public MinigameMap getMap() {
		return getMap(getName());
	}

	public String getName() {
		return name;
	}

	/**
	 * Pega o MinigamePlayer referente ao jogador e se caso não exista cria um
	 * 
	 * @param player Jogador
	 * @return Instancia de MinigamePlayer (MP)
	 */
	public MinigamePlayer getPlayer(Player player) {
		MinigamePlayer miniplayer;
		for (MinigamePlayer p : players) {
			if (player.equals(p.getPlayer())) {
				return p;
			}
		}
		miniplayer = new MinigamePlayer(player);
		players.add(miniplayer);

		return miniplayer;
	}

	/**
	 * Pega os jogadores que estão jogando
	 * 
	 * @return Lista de Jogadores ({@link Player})
	 */
	public List<Player> getPlayersOnline() {
		List<Player> list = new ArrayList<>();
		for (MinigamePlayer player : players) {
			list.add(player.getPlayer());
		}

		// List<Player> listaDoPlayers =
		// players.stream().map(MinigamePlayer::getPlayer).collect(Collectors.toList());
		return list;
	}

	/**
	 * Verifica se existe o lobby
	 * 
	 * @return
	 */
	public boolean hasLobby() {
		return lobby != null;
	}

	/**
	 * Verifica se o jogador esta no modo Admin
	 * 
	 * @param player Jogador
	 * @return
	 */
	public boolean isAdmin(Player player) {
		return getPlayer(player).isState(MinigamePlayerState.ADMIN);

	}

	/**
	 * Verifica se é modo Bungeecord
	 * 
	 * @return
	 */
	public boolean isBungeecord() {
		return bungeecord;
	}

	/**
	 * Verifica se o minigame esta ativado
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Verifica se o Jogador esta no modo Normal (sem ser Admin ou Spectador)
	 * 
	 * @param player Jogador
	 * @return
	 */
	public boolean isPlayer(Player player) {
		return getPlayer(player).isState(MinigamePlayerState.NORMAL);

	}

	/**
	 * Verifica se o jogador esta no Minigame
	 * 
	 * @param player Jogador
	 * @return
	 */
	public boolean isPlaying(Player player) {
		return getPlayer(player).isPlaying();
	}

	/**
	 * Verifica se o Jogador esta no modo Spectador
	 * 
	 * @param player Jogador
	 * @return
	 */
	public boolean isSpectator(Player player) {
		return getPlayer(player).isState(MinigamePlayerState.SPECTATOR);

	}

	/**
	 * Verifica se o Estado da Sala principal é igual este estado
	 * 
	 * @param state Estado
	 * @return
	 */
	public boolean isState(MinigameState state) {
		return getGame().isState(state);
	}

	/**
	 * Entrar em uma Sala
	 * 
	 * @param game   Sala
	 * @param player Jogador
	 */
	public void joinPlayer(MinigameRoom game, Player player) {
		MinigamePlayer p = getPlayer(player);
		p.join(game);

	}

	/**
	 * Entrar em um Time
	 * 
	 * @param team   Time
	 * @param player Jogador
	 */
	public void joinPlayer(MinigameTeam team, Player player) {
		MinigamePlayer p = getPlayer(player);
		p.join(team);
	}

	/**
	 * Remover o jogador da sala e do time Atual dele
	 * 
	 * @param player Jogador
	 */
	public void leavePlayer(Player player) {
		MinigamePlayer p = getPlayer(player);
		if (p.isPlaying()) {
			p.getGame().leave(p);
		}

		if (p.hasTeam()) {
			p.getTeam().leave(p);
		}

	}

	/**
	 * Remove o jogador da Lista de jogadores {@link MinigamePlayer}
	 * 
	 * @param player Jogador
	 */
	public void remove(Player player) {
		players.remove(getPlayer(player));
	}

	/**
	 * Remove a Sala da lista de salas existentes
	 * 
	 * @param game Sala
	 */
	public void removeGame(MinigameRoom game) {
		this.rooms.remove(game);
	}

	/**
	 * Remove a sala pelo seu ID
	 * 
	 * @param id
	 */
	public void removeGame(int id) {
		MinigameRoom game = getRoom(id);
		this.rooms.remove(game);
	}

	public Object restore(Map<String, Object> map) {

		return null;
	}

	/**
	 * Metodo que é executado a cada segundo e executa o metodo de cada sala
	 * <code> events(room)</code>
	 */
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

	public String getMessagePrefix() {
		return messagePrefix;
	}

	public void setMessagePrefix(String messagePrefix) {
		this.messagePrefix = messagePrefix;
	}

	public List<MinigameLobby> getLobbies() {
		return lobbies;
	}

	public void setLobbies(List<MinigameLobby> lobbies) {
		this.lobbies = lobbies;
	}

	public int getMaxPlayersPerLobby() {
		return maxPlayersPerLobby;
	}

	public void setMaxPlayersPerLobby(int maxPlayersPerLobby) {
		this.maxPlayersPerLobby = maxPlayersPerLobby;
	}

}