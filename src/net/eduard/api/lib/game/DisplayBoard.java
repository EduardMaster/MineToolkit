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

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.modules.Extra;

/**
 * API de criação de Scoreboard feita para facilitar sua vida
 *
 * @author Eduard
 */
@SuppressWarnings("unused")
public class DisplayBoard {
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
        getScoreboard();
    }

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
    private static final int TITLE_LIMIT = 32;

    /**
     * Tamanh máximo de um Prefixo do {@link Team} da Scoreboard
     */
    private static final int PREFIX_LIMIT = 16;
    /**
     * Tamanh máximo de um Suffix do {@link Team} da Scoreboard
     */
    private static final int SUFFIX_LIMIT = 16;
    /**
     * Limite atual do nome do Jogador
     */
    private int PLAYER_NAME_LIMIT = PLAYER_BELOW_1_8_NAME_LIMIT;
    /**
     * Linhas da Scoreboard
     */
    private List<String> lines = new ArrayList<>();
    /**
     * Titulo da scoreboard
     */
    private String title;
    /**
     * Barra de vida encima da cabeça do jogador
     */
    private String healthBar;
    /**
     * Se a scoreboard não irá piscar de Jeito nenhum (se ela é perfeita)
     */
    private boolean perfect;
    /**
     * {@link Objective} que armazena a Vida dos jogadores
     */

    protected transient Objective health;

    private boolean healthBarEnabled;
    /**
     * {@link Scoreboard} criada
     */

    private transient Scoreboard scoreboard;
    /**
     * {@link Objective} que armazena as linhas da Scoreboard
     */

    private transient Objective objective;

    /**
     * HashMap armazenando os nomes dos jgoadores Fakes (As Linhas)
     */

    private transient Map<Integer, OfflinePlayer> fakes = new HashMap<>();
    /**
     *
     */

    private transient Map<Integer, Team> teams = new HashMap<>();

    private transient Map<Integer, String> texts = new HashMap<>();

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
     *
     * @return
     */
    public DisplayBoard clear() {
        for (int id = 15; id > 0; id--) {
            remove(id);
        }
        return this;
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
        getScoreboard();
        if (center.isEmpty()) {
            center = "" + ChatColor.values()[line - 1];
        }
        prefix = Extra.cutText(prefix, PREFIX_LIMIT);
        center = Extra.cutText(center, PLAYER_ABOVE_1_7_NAME_LIMIT);
        suffix = Extra.cutText(suffix, SUFFIX_LIMIT);
        Team team = teams.get(line);
        boolean needUpdate = true;
        if (fakes.containsKey(line)) {
            OfflinePlayer fake = fakes.get(line);
            if (!fake.getName().equals(center)) {
                team.removePlayer(fake);
                if (fakes.size() >= 15) {
                    objective.getScore(fake).setScore(-1);
                } else {
                    scoreboard.resetScores(fake);
                }
            }else needUpdate = false;
        }
        FakePlayer fake = new FakePlayer(center);
        if (needUpdate) {
            objective.getScore(fake).setScore(line);
            fakes.put(line, fake);
            team.addPlayer(fake);
        }

        team.setSuffix(suffix);
        team.setPrefix(prefix);

    }


    /**
     * Returna uma Copia da Scoreboard
     */
    public DisplayBoard copy() {
        DisplayBoard board = new DisplayBoard(this.title);
        board.getLines().addAll(this.lines);

        return board;
    }


    public DisplayBoard update(Player player) {
        int id = 15;
        for (String line : this.lines) {
            set(id, Mine.getReplacers(line, player));
            id--;
        }
        setDisplay(Mine.getReplacers(title, player));
        removeTrash();
        return this;
    }

    public DisplayBoard update() {
        setDisplay(title);
        int id = 15;
        for (String line : lines) {
            set(id, line);
            id--;
        }

        removeTrash();

        return this;
    }

    protected void toTrash(OfflinePlayer player){
        objective.getScore(player).setScore(-1);
    }

    protected void removeTrash(){
        for (OfflinePlayer player : scoreboard.getPlayers()){
            if (objective.getScore(player).getScore() == -1){
                scoreboard.resetScores(player);
            }
        }
    }

    public Scoreboard getScoreboard() {

        if (scoreboard == null) {
            if (Bukkit.getScoreboardManager() != null) {
                this.fakes = new HashMap<>();
                this.teams = new HashMap<>();
                this.texts = new HashMap<>();

                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                objective = scoreboard.registerNewObjective("displayBoard", "dummy");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                if (healthBarEnabled) {
                    health = scoreboard.registerNewObjective("HealthBar", Criterias.HEALTH);
                    health.setDisplaySlot(DisplaySlot.BELOW_NAME);
                }
                for (int id = 15; id > 0; id--) {
                    Team team = scoreboard.registerNewTeam("displayTeam" + id);

                    teams.put(id, team);

                }
                setDisplay(title);
                setHealthBar(Mine.getRedHeart());
            }
        }
        return scoreboard;
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
     * @return
     */
    public DisplayBoard empty(int slot) {
        set(id(slot), "");

        return this;
    }

    /**
     * Remove a linha da Scoreboard
     *
     * @param slot Linha
     * @return
     */
    public DisplayBoard clear(int slot) {
        int id = id(slot);
        remove(id);
        return this;
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


    public Objective getBoard() {
        return objective;
    }

    public Objective getHealth() {
        return health;
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


    public Map<Integer, OfflinePlayer> getFakes() {
        return fakes;
    }


    public Map<Integer, Team> getTeams() {
        return teams;
    }


    public Map<Integer, String> getTexts() {
        return texts;
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
