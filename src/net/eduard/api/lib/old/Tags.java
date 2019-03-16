package net.eduard.api.lib.old;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.lib.game.Tag;

/**
 * Tag do jogador<br>
 * Versão anterior {@link TagSetup} 1.0
 * @version 2.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão Atual {@link Tag}
 *
 */
public class Tags implements Listener {
	public static HashMap<Player, Tag> tags = new HashMap<>();

	public static void enable() {

		new EventSetup(20, 100000) {

			public void run() {

				for (Player p : Principal.getOnlinePlayers()) {
					{

						Scoreboard scoreboard = p.getScoreboard();
						if ((scoreboard == null)
								|| (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard()))) {
							continue;
						}
						Iterator<Entry<Player, Tag>> localIterator2 = Tags.tags.entrySet().iterator();
						while (localIterator2.hasNext()) {
							Entry<Player, Tag> teams = localIterator2.next();
							String name = teams.getKey().getName();
							if (scoreboard.getTeam(name) == null) {
								scoreboard.registerNewTeam(name);
							}
							Team team = scoreboard.getTeam(name);
							Tag tag = teams.getValue();
							team.setPrefix(tag.getPrefix());
							team.setSuffix(tag.getSuffix());

							OfflinePlayer player = Bukkit.getOfflinePlayer(name);
							if (!team.hasPlayer(player)) {
								team.addPlayer(player);
							}
						}
					}

					Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
					for (Object tags : Tags.tags.entrySet()) {
						String name = ((Player) ((Entry<?, ?>) tags).getKey()).getName();
						if (scoreboard.getTeam(name) == null) {
							scoreboard.registerNewTeam(name);
						}
						Team team = scoreboard.getTeam(name);
						Tag tag = (Tag) ((Entry<?, ?>) tags).getValue();
						team.setPrefix(tag.getPrefix());
						team.setSuffix(tag.getSuffix());
						OfflinePlayer player = Bukkit.getOfflinePlayer(name);
						if (!team.hasPlayer(player)) {
							team.addPlayer(player);
						}
					}
				}
			}
		};
	}

}
