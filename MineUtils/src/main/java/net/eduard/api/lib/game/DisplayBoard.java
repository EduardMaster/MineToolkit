package net.eduard.api.lib.game;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.modules.Mine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * API de criação de Scoreboard feita para facilitar sua vida
 * <br>
 * Updates
 * <br>
 * v1.1 Suporte a Animação de Frames a cada ticks
 *
 * @author Eduard
 * @version 1.1
 */
@SuppressWarnings("unused")
public class DisplayBoard implements Cloneable {


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

    public static class DisplayBoardScrollPart implements Cloneable {
        private List<String> lines = new ArrayList<>();

        public List<String> getLines() {
            return lines;
        }
    }

    public DisplayBoardScroll addScroll(int size) {
        DisplayBoardScroll scroll = new DisplayBoardScroll();
        scroll.setStartPosition(15 - getLines().size());
        for (int index = 0; index < size; index++) {
            add("Scroll-" + index);
        }
        getScrolls().add(scroll);
        return scroll;
    }

    public static class DisplayBoardScroll implements Cloneable {

        private int startPosition = 15;
        private int ticks = 5;
        private transient int current = 0;
        private transient long lastModification = 0;
        private List<DisplayBoardScrollPart> parts = new ArrayList<>();

        public int getTicks() {
            return ticks;
        }

        public void setTicks(int ticks) {
            this.ticks = ticks;
        }

        public void setStartPosition(int startPosition) {
            this.startPosition = startPosition;
        }

        public List<DisplayBoardScrollPart> getParts() {
            return parts;
        }

        public void add(String... lines) {
            DisplayBoardScrollPart part = new DisplayBoardScrollPart();
            int start = startPosition;
            for (String line : lines) {
                part.getLines().add(line);
                start--;
            }
            parts.add(part);

        }


        public int getStartPosition() {
            return startPosition;
        }

        public DisplayBoardScrollPart get() {
            if (parts.isEmpty()) {
                return null;
            }
            long duration = ticks * 50L;
            long now = System.currentTimeMillis();
            if (lastModification + duration < now) {
                current++;
                if (current >= parts.size()) {
                    current = 0;
                }
                lastModification = now;
            }
            return parts.get(current);
        }

    }


    public DisplayBoard clone() {
        try {
            DisplayBoard clone = (DisplayBoard) super.clone();
            clone.scoreboard = null;
            clone.objective = null;
            clone.health = null;
            clone.getScoreboard();
            return clone;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
     * Titulo da scoreboard
     */
    private String title;
    /**
     * Linhas da Scoreboard
     */
    private List<String> lines = new ArrayList<>();

    private List<DisplayBoardScroll> scrolls = new ArrayList<>();

    public List<DisplayBoardScroll> getScrolls() {
        return scrolls;
    }

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


    private transient Map<Integer, Team> teams = new HashMap<>();

    private transient Map<Integer, String> texts = new HashMap<>();


    public void hide() {
        objective.setDisplaySlot(null);
    }

    public boolean isShowing() {
        return objective.getDisplaySlot() == DisplaySlot.SIDEBAR;
    }

    /**
     * Ativa a scoreboard
     */
    public void show() {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
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
            } else needUpdate = false;
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
        return clone();
    }

    private int toIndex(int position) {
        return 15 - position;
    }

    public void updateScrolls() {
        for (DisplayBoardScroll scroll : getScrolls()) {
            DisplayBoardScrollPart part = scroll.get();
            if (part == null) continue;
            int position = scroll.getStartPosition();
            for (String line : part.getLines()) {
                getLines().set(toIndex(position), line);
                position--;
            }
        }

    }

    /**
     * Método criado dia 25/03/21 para calcular lag de cada função desta classe
     * @param name Nome da Funçõo
     * @param action Runnable da Função
     */
    public void calc(String name, Runnable action) {
        long start = System.currentTimeMillis();
        action.run();
        long end = System.currentTimeMillis();
        long dif = end - start;
        //System.out.println("[ScoreLag] " + name + ": " + dif + "ms");

    }


    public void update(Player player) {

        calc("updateScrools", this::updateScrolls);
        calc("For for lines" ,  ()-> {
        int id = 15;
        for (String line : this.lines) {
            set(id, Mine.getReplacers(line, player));
            id--;
        }
        });
        calc("setDisplay", () ->  setDisplay(Mine.getReplacers(title, player)));

       // calc("removeTrash", () ->   removeTrash());

    }

    public void update() {
        updateScrolls();
        setDisplay(title);
        int id = 15;
        for (String line : lines) {
            set(id, line);
            id--;
        }

        removeTrash();

    }

    protected void toTrash(OfflinePlayer player) {
        objective.getScore(player).setScore(-1);
    }

    /**
     * No dia 25/03/2021 este método foi inutilizado pois dava bug de desempenho na Host Premium
     * e versão do PaperSpigot 1.8.8
     */
    protected void removeTrash() {
        for (String player : scoreboard.getEntries()) {
            if (objective.getScore(player).getScore() == -1) {
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
     */
    public void apply(Player player) {
        player.setScoreboard(scoreboard);

    }

    public void updateHealthBar(Player player) {

        player.setHealth(player.getMaxHealth() - 1);
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

    public void setDisplay(String name) {
        objective.setDisplayName(Extra.cutText(name, TITLE_LIMIT));
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

    private int id(int slot) {
        return slot <= 0 ? 1 : Math.min(slot, 15);
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
