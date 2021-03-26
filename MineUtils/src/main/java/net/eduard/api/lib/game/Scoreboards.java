package net.eduard.api.lib.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;

/**
 * API de Scoreboard com menas opções que DisplayBoard,
 * mais funciona muito bem.
 * <p>
 * Updates</p>
 * <ul>
 *     <li>v1.1 Scoreboard tem cache não atualiza se não precisa e desativado suporte a 1.7</li>
 * </ul>
 *
 * @author Eduard
 * @version 1.1
 */

public class Scoreboards {
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<Integer, String> scores = new HashMap<>();
    private final Map<Integer, Team> teams = new HashMap<>();

    public Scoreboards() {
        this("Scoreboard simples");

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


    public static String cutText(String text, int lenght) {
        return text.length() > lenght ? text.substring(0, lenght) : text;
    }

    public void setLine(String prefix, String center, String suffix, int line) {
        prefix = cutText(prefix, 16);
        center = cutText(center, 40);
        suffix = cutText(suffix, 16);
        Team team = teams.get(line);
        boolean alreadyHave = true;
        if (scores.containsKey(line)) {
            if (!center.equals(scores.get(line))) {
                team.removeEntry(scores.get(line));
                alreadyHave = false;
            }
        } else {
            alreadyHave = false;
        }

        if (!alreadyHave) {
            objective.getScore(center).setScore(line);
            scores.put(line, center);
            team.addEntry(center);
        }
        team.setSuffix(suffix);
        team.setPrefix(prefix);


    }


}
