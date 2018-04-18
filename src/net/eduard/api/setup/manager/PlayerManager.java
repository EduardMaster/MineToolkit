package net.eduard.api.setup.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.VaultAPI;
import net.eduard.api.setup.event.ScoreUpdateEvent;
import net.eduard.api.setup.game.DisplayBoard;
import net.eduard.api.setup.game.FakePlayer;
import net.eduard.api.setup.game.Tag;

public class PlayerManager extends TimeManager {
	private boolean tagsEnabled;
	private boolean tagsByGroup;
	private boolean scoresEnabled;
	private Tag tagDefault;
	private DisplayBoard scoreDefault;
	private transient Map<String, Tag> groupsTags = new HashMap<>();
	private transient Map<Player, Tag> playersTags = new HashMap<>();
	private transient Map<Player, DisplayBoard> playersScores = new HashMap<>();

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		removeScore(p);
		removeTag(p);
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = e.getPlayer();
		removeScore(p);
		removeTag(p);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (scoresEnabled) {
			setScore(e.getPlayer(), scoreDefault.copy());
		}
		if (tagsEnabled) {
			setTag(p, tagDefault.copy());
			updatePlayerTag(p);
		}

	}

	public Map<String, Tag> getGroupsTags() {
		return groupsTags;
	}

	public void updateGroupsTags() {
		for (String group : VaultAPI.getPermission().getGroups()) {
			String prefix = VaultAPI.getChat().getGroupPrefix("null", group);
			String suffix = VaultAPI.getChat().getGroupSuffix("null", group);
			Tag tag = new Tag(prefix, suffix);
			groupsTags.put(group, tag);

		}

	}

	public void updateGroupsRanks(List<String> list) {
		int id = 0;
		for (String group : list) {
			Tag tag = groupsTags.get(group);
			// Mine.broadcast("affs "+tag +" "+(tag==null));
			if (tag != null) {
				tag.setRank(id);
				id++;
			}
			// Mine.broadcast("affs "+tag +" "+(tag==null));
		}

	}

	@SuppressWarnings("deprecation")
	public void updateTags(Scoreboard score) {
		// score.getTeams().forEach(team -> team.unregister());
		for (Entry<Player, Tag> map : playersTags.entrySet()) {
			Tag tag = map.getValue();
			Player player = map.getKey();
			if (player == null)
				continue;
			String name = Mine.cutText(tag.getRank() + player.getName(), 16);
			Team team = Mine.getTeam(score, name);
			try {
				team.setNameTagVisibility(NameTagVisibility.ALWAYS);
			} catch (Exception e) {
			}
			String prefix = Mine.cutText(Mine.toChatMessage(tag.getPrefix()), 16);
			String suffix = Mine.cutText(Mine.toChatMessage(tag.getSuffix()), 16);

			if (!prefix.equals(team.getPrefix()))
				team.setPrefix(prefix);
			if (!suffix.equals(suffix))
				team.setSuffix(suffix);
			FakePlayer fake = new FakePlayer(player.getName());
			if (!team.hasPlayer(fake))
				team.addPlayer(fake);

		}
	}

	public void updatePlayerTag(Player player) {
		if (tagsByGroup) {
			String group = VaultAPI.getPermission().getPrimaryGroup(player);
			Tag tag = groupsTags.get(group);
			if (tag != null) {
				setTag(player, tag.copy());
			}
		}
	}

	public void updateScoreboard(Player player) {
		getScore(player).update(player);
	}

	public void setScore(Player player, DisplayBoard score) {
		playersScores.put(player, score);
		score.apply(player);
	}

	public DisplayBoard getScore(Player player) {
		return playersScores.get(player);

	}

	public Tag getTag(Player player) {
		return playersTags.get(player);
	}

	public void resetTag(Player player) {
		setTag(player, "");
	}

	public void setTag(Player player, String prefix) {
		setTag(player, prefix, "");
	}

	public  void setTag(Player player, String prefix, String suffix) {
		setTag(player, new Tag(prefix, suffix));
	}

	public void setTag(Player player, Tag tag) {
		playersTags.put(player, tag);

	}

	public void updateTagsScores() {
		if (scoresEnabled) {

			try {
				for (Entry<Player, DisplayBoard> map : playersScores.entrySet()) {
					DisplayBoard score = map.getValue();
					Player player = map.getKey();
					ScoreUpdateEvent event = new ScoreUpdateEvent(player, score);
					if (!event.isCancelled()) {
						score.update(player);
					}
				}
			} catch (Exception e) {
				Bukkit.getLogger().info("Falha ao dar update ocorreu uma Troca de Scoreboard no meio do FOR");
			}
		}
		if (tagsEnabled) {

			Scoreboard main = Bukkit.getScoreboardManager().getMainScoreboard();

			for (Player p : Mine.getPlayers()) {
				Scoreboard score = p.getScoreboard();
				if (score == null) {
					p.setScoreboard(main);
					continue;
				}
				updateTags(score);

			}
			updateTags(main);
		}
	}

	public void removeScore(Player player) {
		player.setScoreboard(Mine.getMainScoreboard());
		playersScores.remove(player);
	}

	public void removeTag(Player player) {
		playersTags.remove(player);
	}

	public boolean isTagsEnabled() {
		return tagsEnabled;
	}

	public void setTagsEnabled(boolean tagsEnabled) {
		this.tagsEnabled = tagsEnabled;
	}

	public boolean isTagsByGroup() {
		return tagsByGroup;
	}

	public void setTagsByGroup(boolean tagsByGroup) {
		this.tagsByGroup = tagsByGroup;
	}

	public boolean isScoresEnabled() {
		return scoresEnabled;
	}

	public void setScoresEnabled(boolean scoresEnabled) {
		this.scoresEnabled = scoresEnabled;
	}

	public Tag getTagDefault() {
		return tagDefault;
	}

	public void setTagDefault(Tag tagDefault) {
		this.tagDefault = tagDefault;
	}

	public DisplayBoard getScoreDefault() {
		return scoreDefault;
	}

	public void setScoreDefault(DisplayBoard scoreDefault) {
		this.scoreDefault = scoreDefault;
	}

	public Map<Player, Tag> getPlayersTags() {
		return playersTags;
	}

	public void setPlayersTags(Map<Player, Tag> playersTags) {
		this.playersTags = playersTags;
	}

	public Map<Player, DisplayBoard> getPlayersScores() {
		return playersScores;
	}

	public void setPlayersScores(Map<Player, DisplayBoard> playersScores) {
		this.playersScores = playersScores;
	}

	public void setGroupsTags(Map<String, Tag> groupsTags) {
		this.groupsTags = groupsTags;
	}

	

	

	

	

}
