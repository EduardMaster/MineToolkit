package net.eduard.api.server;

import org.bukkit.entity.Player;

import net.eduard.api.lib.game.DisplayBoard;

public interface ScoreSystem {
	
	void setScore(Player player,DisplayBoard scoreboard);
	void setScoreDefault(Player player);
	DisplayBoard getScore(Player player);
	public boolean hasScore(Player player);

}
