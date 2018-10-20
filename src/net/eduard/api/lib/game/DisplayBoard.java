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
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;

@SuppressWarnings("deprecation")
public class DisplayBoard implements Storable, Copyable {

	public static final int PLAYER_ABOVE_1_7_NAME_LIMIT = 40;
	public static final int PLAYER_BELOW_1_8_NAME_LIMIT = 16;
	public static final int TITLE_LIMIT = 32;
	// public static final int REGISTER_LIMIT = 16;
	public static final int PREFIX_LIMIT = 16;
	public static final int SUFFIX_LIMIT = 16;
	public int PLAYER_NAME_LIMIT = PLAYER_BELOW_1_8_NAME_LIMIT;
	protected List<String> lines = new ArrayList<>();
	protected String title;
	protected String healthBar;
	protected boolean perfect;
	protected transient Objective health;
	protected transient Scoreboard scoreboard;
	protected transient Objective objective;
	protected transient Map<Integer, OfflinePlayer> fakes = new HashMap<>();
	protected transient Map<Integer, Team> teams = new HashMap<>();
	protected transient Map<Integer, String> texts = new HashMap<>();

	public DisplayBoard hide() {

		objective.setDisplaySlot(null);
		return this;
	}

	public boolean isShowing() {
		return objective.getDisplaySlot() == DisplaySlot.SIDEBAR;
	}

	public DisplayBoard show() {
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		return this;
	}

	public void clear() {
		for (int id = 15; id > 0; id--) {
			remove(id);
		}
	}

	public void setLine(String prefix, String center, String suffix, int line) {
		if (center.isEmpty()) {
			center = "" + ChatColor.values()[line - 1];
		}
		prefix = Mine.cutText(prefix, 16);
		center = Mine.cutText(center, 40);
		suffix = Mine.cutText(suffix, 16);
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


	public DisplayBoard copy() {
		return copy(this);
	}

	public DisplayBoard() {
		this("§6§lScoreboard");
		
	}

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
		Mine.broadcast("§cinit executado"
				+ "");
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = scoreboard.registerNewObjective("sc"+Mine.getRandomInt(1000, 100000), "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		health = scoreboard.registerNewObjective("HealthBar", Criterias.HEALTH);
		health.setDisplaySlot(DisplaySlot.BELOW_NAME);
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
		return this;
	}

	public char getHeart() {
		return '\u2764';
	}

	public DisplayBoard apply(Player player) {
		player.setScoreboard(scoreboard);
		return this;
	}

	public DisplayBoard updateHealthBar(Player player) {
		player.setHealth(player.getMaxHealth() - 1);
		return this;
	}

	public void empty(int slot) {
		set(id(slot), "");
	}

	public void clear(int slot) {
		int id = id(slot);
		remove(id);
	}

	public DisplayBoard setDisplay(String name) {
		objective.setDisplayName(Mine.cutText(name, TITLE_LIMIT));
		return this;
	}

	public boolean remove(int id) {
		OfflinePlayer fake = fakes.get(id);
		if (fake==null)return false;
		scoreboard.resetScores(fake);
		Team team = teams.get(id);
		if (team!=null) {
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
		text = Mine.cutText(text, PREFIX_LIMIT + SUFFIX_LIMIT + PLAYER_NAME_LIMIT);
		String center = "";
		String prefix = "";
		String suffix = "";
		if (text.length() > PLAYER_NAME_LIMIT + PREFIX_LIMIT + SUFFIX_LIMIT) {
			text = Mine.cutText(text, PLAYER_NAME_LIMIT + PREFIX_LIMIT + SUFFIX_LIMIT);
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
			prefix = Mine.cutText(text, 16);

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
		this.health.setDisplayName(health);
		this.healthBar = health;
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
		Mine.broadcast("§cOn Copy edxecutado");
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

}
