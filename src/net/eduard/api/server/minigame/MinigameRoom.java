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

	@StorageAttributes(reference = true)
	private Minigame minigame;
	@StorageAttributes(reference = true)
	private MinigameMap map;
	private MinigameMode mode = MinigameMode.NORMAL;
	private int id;
	private int time;
	private boolean enabled;
	private int round;
	private transient MinigameState state = MinigameState.STARTING;
	private transient MinigameMap mapUsed;

	private MinigamePlayer secondWinner;
	private MinigamePlayer thirdWinner;
	private transient List<MinigamePlayer> players = new ArrayList<>();
	private transient List<MinigamePlayer> losers = new ArrayList<>();
	private transient List<MinigameTeam> teams = new ArrayList<>();

	public MinigameRoom() {
	}

	/**
	 * Manda a mensagem para todos os jogadores participando da Sala
	 * 
	 * @param message
	 */
	public void broadcast(String message) {
		for (MinigamePlayer player : players) {
			player.send(minigame.getMessagePrefix()
					+ message.replace("$time", Mine.getTime(time)).replace("$max", "" + getMap().getMaxPlayersAmount())
							.replace("$players", "" + getPlayers().size()));
		}
	}
	public boolean hasMinPlayersAmount() {
		return getPlayers().size() >= getMap().getMinPlayersAmount();
	}
	/**
	 * Verifica se o jogador esta jogando nesta Sala
	 * 
	 * @param player Jogador
	 * @return
	 */
	public boolean isPlaying(Player player) {
		return players.stream().filter(p -> p.getPlayer().equals(player)).findFirst().isPresent();
	}

	public List<Player> getPlayersOnline() {

		return players.stream().filter(p -> p.isOnline()).map(p -> p.getPlayer()).collect(Collectors.toList());
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

	/**
	 * Aumenta 1 segundo na contagem
	 * 
	 * @return
	 */
	public int advance() {
		return ++time;
	}

	/**
	 * Diminui 1 segundo da contagem
	 * 
	 * @return
	 */
	public int decrease() {
		return --time;
	}

	/**
	 * Coloca o estado desta sala em Jogando (A batalha vai começar)
	 */
	public void startGame() {
		setTime(getMinigame().getTimeOnStartTimer());
		setState(MinigameState.PLAYING);
	}

	/**
	 * Coloca o estado desta sala em Equipando (Pre jogo de muitos minigames)
	 */
	public void startPreGame() {
		setTime(getMinigame().getTimeIntoPlay());
		setState(MinigameState.EQUIPPING);
	}

	/**
	 * Coloca o estado desta sala em Acabando (estado usado em alguns eventos
	 * apenas)
	 */
	public void ending() {
		setTime(getMinigame().getTimeOnRestartTimer());
		setState(MinigameState.ENDING);
	}

	/**
	 * Coloca o estado desta sala em Reiniciando <br>
	 * 'quando fazer eventos use este estado para saber que o evento esta desligado'
	 */
	public void restarting() {
		setState(MinigameState.RESTARTING);
		setTime(getMinigame().getTimeIntoRestart());
	}

	/**
	 * Coloca o estado desta sala em Iniciando
	 */
	public void restart() {
		setState(MinigameState.STARTING);
		setTime(getMinigame().getTimeIntoStart());
	}

	/**
	 * Remove o jogador desta Sala
	 * 
	 * @param player
	 */
	public void leave(MinigamePlayer player) {
		player.getGame().getPlayers().remove(player);
		player.setGame(null);
		for (MinigamePlayer jogador : getPlayers()) {
			jogador.hide(player);
			player.hide(jogador);
		}
	}

	public void leaveAll() {
		Iterator<MinigamePlayer> it = players.iterator();
		while (it.hasNext()) {
			MinigamePlayer player = it.next();
			player.setGame(null);
			it.remove();
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

	/**
	 * Remove os jogadores de todos os Times
	 */
	public void emptyTeams() {
		for (MinigameTeam team : teams) {
			team.leaveAll();
		}
	}

	/**
	 * Força a entrada do jogador na Sala
	 * 
	 * @param player Jogador
	 */
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

	/**
	 * Verifica se tem Espaço na sala para novos jogadores
	 * 
	 * @return
	 */
	public boolean hasSpace() {
		return getPlayers().size() < getMap().getMaxPlayersAmount();
	}

	public MinigamePlayer getSecondWinner() {
		return secondWinner;
	}

	public void setSecondWinner(MinigamePlayer secondWinner) {
		this.secondWinner = secondWinner;
	}

	public MinigamePlayer getThirdWinner() {
		return thirdWinner;
	}

	public void setThirdWinner(MinigamePlayer thirdWinner) {
		this.thirdWinner = thirdWinner;
	}

	public List<MinigamePlayer> getLosers() {
		return losers;
	}

	public void setLosers(List<MinigamePlayer> losers) {
		this.losers = losers;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}