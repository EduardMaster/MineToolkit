package net.eduard.api.bungee.party;

import java.util.ArrayList;

public class Party {
	
	private String leader;
	private long createdSince;
	private ArrayList<String> players = new ArrayList<>();
	public void disband() {
		players.clear();
		setLeader(null);
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}


	public long getCreatedSince() {
		return createdSince;
	}

	public void setCreatedSince(long createdSince) {
		this.createdSince = createdSince;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

}
