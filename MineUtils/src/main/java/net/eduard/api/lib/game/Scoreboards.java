package net.eduard.api.lib.game;

import net.eduard.api.lib.modules.FakePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

/**
 * API de Scoreboard com menas opções que DisplayBoard,
 * mais funciona muito bem.
 *
 * @author Eduard
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class Scoreboards {
    private Scoreboard scoreboard;
    private Objective objective;
    public Scoreboards(){

    }
    public Scoreboards(String title) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("scoreboard", "dummy");
        objective.setDisplayName(title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int id = 15; id > 0; id--) {
            Team team = scoreboard.registerNewTeam("team-" + id);
            teams.put(id, team);
        }
        emptyLines();

    }

    public void emptyLines() {
        for (int id = 15; id > 0; id--) {
            emptyLine(id);
        }
    }

    public void emptyLine(int id) {
        setLine("", "" + ChatColor.values()[id - 1], "", id);
    }


    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    private final Map<Integer, OfflinePlayer> fakes = new HashMap<>();
    private final Map<Integer, Team> teams = new HashMap<>();

    public static String cutText(String text, int lenght) {
        return text.length() > lenght ? text.substring(0, lenght) : text;
    }

    public void setLine(String prefix, String center, String suffix, int line) {
        prefix = cutText(prefix, 16);
        center = cutText(center, 40);
        suffix = cutText(suffix, 16);
        Team team = teams.get(line);
        if (fakes.containsKey(line)) {
            OfflinePlayer fake = fakes.get(line);
            team.removePlayer(fake);
            objective.getScore(fake).setScore(-1);

        }
        FakePlayer fake = new FakePlayer(center);
        objective.getScore(fake).setScore(line);
        fakes.put(line, fake);
        team.addPlayer(fake);
        team.setSuffix(suffix);
        team.setPrefix(prefix);
        clearScoreboard();

    }

    /**
     * Método parecido com do removeTrash do DisplayBoard
     */
    public void clearScoreboard() {
        for (OfflinePlayer fake : scoreboard.getPlayers()) {
            Score score = objective.getScore(fake);
            if (score.getScore() == -1)
                scoreboard.resetScores(fake);
        }
    }

}
