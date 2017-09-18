package net.eduard.api.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class GameTeam {
private Player leader;
private int maxSize = 5;
private boolean friendlyFire;
private List<Player> players = new ArrayList<>();
public Player getLeader() {
	return leader;
}
public void setLeader(Player leader) {
	this.leader = leader;
}
public int getMaxSize() {
	return maxSize;
}
public void setMaxSize(int maxSize) {
	this.maxSize = maxSize;
}
public List<Player> getPlayers() {
	return players;
}
public void setPlayers(List<Player> players) {
	this.players = players;
}
public boolean isFriendlyFire() {
	return friendlyFire;
}
public void setFriendlyFire(boolean friendlyFire) {
	this.friendlyFire = friendlyFire;
}

}
