package net.eduard.api.tutorial.sistemas;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.setup.ScoreAPI.DisplayBoard;

public class CriarDisplayboard {

	private static DisplayBoard board;
	public static void criar() {
		board = new DisplayBoard("titulo", "linha1", "linha2");
		board.setTitle("mudando_titulo");
		// mudar
		board.set(15, "mudando linha 1");
		// deixa em branco slot
		board.empty(14);
		// remover slot
		board.clear(15);
	}
	public static void aplicando(Player jogador) {
		board.apply(jogador);;
	}
	public static void newScoreboard(String title, String... lines) {
		Scoreboard score = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = score.registerNewObjective("scoreboard", "dummy");
		objective.setDisplayName(
				title.length() > 32 ? title.substring(0, 32) : title);
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		int id = 0;
		for (String line : lines) {
			Team team = score.registerNewTeam("team" + id);
			team.setPrefix(line);
			team.setNameTagVisibility(NameTagVisibility.NEVER);
			id++;
		}
	}

}
