package net.eduard.api.lib.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.Copyable;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;

/**
 * API de criação de Scoreboard feita para facilitar sua vida
 * 
 * @author Eduard
 *
 */
@SuppressWarnings("deprecation")
public class DisplayBoard implements Storable, Copyable {

	/**
	 * Tamanho limite do Nome do Jogador Linha Acima da 1.7
	 */
	public static final int PLAYER_ABOVE_1_7_NAME_LIMIT = 40;
	/**
	 * Tamanho limite do Nome do Jogador Abaixo da 1.8
	 */
	public static final int PLAYER_BELOW_1_8_NAME_LIMIT = 16;
	/**
	 * Tamanho máximo do Titulo da Scoreboard
	 */
	public static final int TITLE_LIMIT = 32;
	// public static final int REGISTER_LIMIT = 16;
	/**
	 * Tamanh máximo de um Prefixo do {@link Team} da Scoreboard
	 */
	public static final int PREFIX_LIMIT = 16;
	/**
	 * Tamanh máximo de um Suffix do {@link Team} da Scoreboard
	 */
	public static final int SUFFIX_LIMIT = 16;
	/**
	 * Limite atual do nome do Jogador
	 */
	public int PLAYER_NAME_LIMIT = PLAYER_BELOW_1_8_NAME_LIMIT;
	/**
	 * Linhas da Scoreboard
	 */
	protected List<String> lines = new ArrayList<>();
	/**
	 * Titulo da scoreboard
	 */
	protected String title;
	/**
	 * Barra de vida encima da cabeça do jogador
	 */
	protected String healthBar;
	/**
	 * Se a scoreboard não irá piscar de Jeito nenhum (se ela é perfeita)
	 */
	protected boolean perfect;
	/**
	 * {@link Objective} que armazena a Vida dos jogadores
	 */
	@NotCopyable
	protected transient Objective health;

	private boolean healthBarEnabled;
	/**
	 * {@link Scoreboard} criada
	 */
	@NotCopyable
	protected transient Scoreboard scoreboard;
	/**
	 * {@link Objective} que armazena as linhas da Scoreboard
	 */
	@NotCopyable
	protected transient Objective objective;

	/**
	 * HashMap armazenando os nomes dos jgoadores Fakes (As Linhas)
	 */
	@NotCopyable
	protected transient Map<Integer, OfflinePlayer> fakes = new HashMap<>();
	/**
	 * 
	 */
	@NotCopyable
	protected transient Map<Integer, Team> teams = new HashMap<>();
	@NotCopyable
	protected transient Map<Integer, String> texts = new HashMap<>();

	public DisplayBoard hide() {

		objective.setDisplaySlot(null);
		return this;
	}

	public boolean isShowing() {
		return objective.getDisplaySlot() == DisplaySlot.SIDEBAR;
	}

	/**
	 * Ativa a scoreboard
	 * 
	 * @return A classe
	 */
	public DisplayBoard show() {
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		return this;
	}

	/**
	 * Remove todas as linhas da Scoreboard
	 */
	public void clear() {
		for (int id = 15; id > 0; id--) {
			remove(id);
		}
	}

	/**
	 * Seta uma linha da Scoreboard
	 * 
	 * @param prefix Texto antes do centro
	 * @param center Texto que fica no meio
	 * @param suffix Texto posterior ao Meio
	 * @param line   Numero da Linha
	 */
	public void setLine(String prefix, String center, String suffix, int line) {
		if (center.isEmpty()) {
			center = "" + ChatColor.values()[line - 1];
		}
		prefix = Extra.cutText(prefix, 16);
		center = Extra.cutText(center, 40);
		suffix = Extra.cutText(suffix, 16);
		Team team = teams.get(line);
		if (fakes.containsKey(line)) {
			OfflinePlayer fake = fakes.get(line);
			if (!fake.getName().equals(center)) {
				team.removePlayer(fake);
				if (fakes.size() >= 15) {
					objective.getScore(fake).setScore(-1);
				} else {
					scoreboard.resetScores(fake);
				}
				fakes.remove(line);
			}
		}
		if (!fakes.containsKey(line)) {
			FakePlayer fake = new FakePlayer(center);

			objective.getScore(fake).setScore(line);
			fakes.put(line, fake);
			team.addPlayer(fake);
		}
		team.setSuffix(suffix);
		team.setPrefix(prefix);

	}

	/**
	 * Remove algumas Entradas da Scoreboard para reduzir o Lag
	 * 
	 * @param force Remover Tudo
	 */
	public void clearEntries(boolean force) {
		scoreboard.getPlayers().stream().forEach(fake -> {
			if (force) {
				scoreboard.resetScores(fake);
			} else if (objective.getScore(fake).getScore() == -1)
				scoreboard.resetScores(fake);
		});
//		for (Iterator<OfflinePlayer> iterator = scoreboard.getPlayers().iterator(); iterator.hasNext();) {
//			OfflinePlayer fake = iterator.next();
//			if (objective.getScore(fake).getScore() == -1)
//				scoreboard.resetScores(fake);
//		}

	}

	/**
	 * Returna uma Copia da Scoreboard
	 */
	public DisplayBoard copy() {
		return copy(this);
	}

	/**
	 * Construtor vazio setando o nome da Scoreboard de '§6§lScoreboard'
	 */
	public DisplayBoard() {
		this("§6§lScoreboard");

	}

	/**
	 * Cosntrutor pedindo o Titulo e uma lista de Linhas seguida por ,
	 * 
	 * @param title Titulo
	 * @param lines Linhas
	 */
	public DisplayBoard(String title, String... lines) {
		setTitle(title);
		getLines().addAll(Arrays.asList(lines));
		init();
	}

	public DisplayBoard update(Player player) {
		int id = 15;
		for (String line : this.lines) {
			set(id, Mine.getReplacers(line, player));
			id--;
		}
		setDisplay(Mine.getReplacers(title, player));
		clearEntries(false);
		return this;
	}

	public DisplayBoard update() {
		setDisplay(title);
		int id = 15;
		for (String line : lines) {
			set(id, line);
			id--;
		}
		clearEntries(false);
		return this;
	}

	public DisplayBoard init() {
//		Mine.broadcast("§cinit executado"
//				+ "");
		if (Bukkit.getScoreboardManager()!=null) {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			objective = scoreboard.registerNewObjective("sc" + Extra.getRandomInt(1000, 100000), "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			if (healthBarEnabled) {
				health = scoreboard.registerNewObjective("HealthBar", Criterias.HEALTH);
				health.setDisplaySlot(DisplaySlot.BELOW_NAME);
			}
			for (int id = 15; id > 0; id--) {
				Team team = scoreboard.registerNewTeam("t" + +Mine.getRandomInt(1000, 100000));
//			FakePlayer fake = new FakePlayer("" + ChatColor.values()[id]);
//			team.addPlayer(fake);
//			objective.getScore(fake).setScore(id);
				teams.put(id, team);

//			fakes.put(id, fake);
			}
			setDisplay(title);
			setHealthBar(Mine.getRedHeart());
		}
		return this;
	}

	/**
	 * Coração: '\u2764'
	 * 
	 * @return Simbolo do Coração
	 */
	public char getHeart() {
		return '\u2764';
	}

	/**
	 * Aplica a Scoreboard no Jogador
	 * 
	 * @param player Jogador
	 * @return A classe
	 */
	public DisplayBoard apply(Player player) {
		player.setScoreboard(scoreboard);
		return this;
	}

	public DisplayBoard updateHealthBar(Player player) {
		player.setHealth(player.getMaxHealth() - 1);
		return this;
	}

	/**
	 * Deixa a linha da Scoreboard vazia
	 * 
	 * @param slot Linha
	 */
	public void empty(int slot) {
		set(id(slot), "");
	}

	/**
	 * Remove a linha da Scoreboard
	 * 
	 * @param slot Linha
	 */
	public void clear(int slot) {
		int id = id(slot);
		remove(id);
	}

	public DisplayBoard setDisplay(String name) {
		objective.setDisplayName(Extra.cutText(name, TITLE_LIMIT));
		return this;
	}

	public boolean remove(int id) {
		OfflinePlayer fake = fakes.get(id);
		if (fake == null)
			return false;
		scoreboard.resetScores(fake);
		Team team = teams.get(id);
		if (team != null) {
			team.removePlayer(fake);
		}
		fakes.remove(id);
		texts.remove(id);
		return false;
	}

	public boolean set(int slot, String text) {
		int id = id(slot);
		String line = texts.get(id);
		if (line != null && line.equals(text)) {
			return true;
		}
		text = Extra.cutText(text, PREFIX_LIMIT + SUFFIX_LIMIT + PLAYER_NAME_LIMIT);
		String center = "";
		String prefix = "";
		String suffix = "";
		if (text.length() > PLAYER_NAME_LIMIT + PREFIX_LIMIT + SUFFIX_LIMIT) {
			text = Extra.cutText(text, PLAYER_NAME_LIMIT + PREFIX_LIMIT + SUFFIX_LIMIT);
		}
		if (text.length() <= PLAYER_NAME_LIMIT) {
			center = text;
		} else if (text.length() <= PLAYER_NAME_LIMIT + PREFIX_LIMIT) {
			center = text.substring(0, PLAYER_NAME_LIMIT);
			suffix = text.substring(SUFFIX_LIMIT);

		} else if (text.length() <= PLAYER_NAME_LIMIT + PREFIX_LIMIT + SUFFIX_LIMIT) {
			prefix = text.substring(0, PREFIX_LIMIT);
			center = text.substring(PREFIX_LIMIT, PREFIX_LIMIT + PLAYER_NAME_LIMIT - 1);
			suffix = text.substring(PREFIX_LIMIT + PLAYER_NAME_LIMIT);
		}
		Team team = teams.get(id);
		if (perfect) {
			prefix = Extra.cutText(text, 16);

			if (text.length() > 16) {
				suffix = text.substring(16);
			}
			team.setPrefix(prefix);
			team.setSuffix(suffix);
		} else {
			setLine(prefix, center, suffix, id);
		}

		texts.put(id, text);

		return true;

	}

	protected int id(int slot) {
		return slot <= 0 ? 1 : slot >= 15 ? 15 : slot;
	}

	public String getDisplay() {
		return objective.getDisplayName();
	}

	public void setHealthBar(String health) {
		if (healthBarEnabled) {
			this.health.setDisplayName(health);
			this.healthBar = health;
		}
	}

	public String getHealthBar() {
		return this.healthBar;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Scoreboard getScore() {
		return scoreboard;
	}

	public Objective getBoard() {
		return objective;
	}

	public Objective getHealth() {
		return health;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		update();
		return null;
	}

//	@Override
	public void onCopy() {
		Mine.console("§fDisplayBoard executando onCopy()");
		init();

	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	public boolean isPerfect() {
		return perfect;
	}

	public void setPerfect(boolean perfect) {
		this.perfect = perfect;
	}

	public Objective getObjective() {
		return objective;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

	public Map<Integer, OfflinePlayer> getFakes() {
		return fakes;
	}

	public void setFakes(Map<Integer, OfflinePlayer> fakes) {
		this.fakes = fakes;
	}

	public Map<Integer, Team> getTeams() {
		return teams;
	}

	public void setTeams(Map<Integer, Team> teams) {
		this.teams = teams;
	}

	public Map<Integer, String> getTexts() {
		return texts;
	}

	public void setTexts(Map<Integer, String> texts) {
		this.texts = texts;
	}

	public void setHealth(Objective health) {
		this.health = health;
	}

	public void add(String line) {
		getLines().add(line);

	}

	public boolean isHealthBarEnabled() {
		return healthBarEnabled;
	}

	public void setHealthBarEnabled(boolean healthBarEnabled) {
		this.healthBarEnabled = healthBarEnabled;
	}

}
