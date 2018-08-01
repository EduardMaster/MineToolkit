package net.eduard.api.server.party;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

public class Party {

	private Player leader;
	private List<Player> members = new ArrayList<>();
	private List<Player> invites = new ArrayList<>();
	private int limit;
	private boolean open;

	public Party(Player leader) {
		setLeader(leader);
		members.add(leader);
	}
	public Party() {
		// TODO Auto-generated constructor stub
	}



	public void clean() {
		for (Iterator<Player> iterator = members.iterator(); iterator.hasNext();) {
			Player m = iterator.next();
			if (!m.isOnline()) {
				iterator.remove();
			}
		}
	}



	public void disband() {
		if (members.size() >= 2) {
			members.remove(leader);
			leader = members.get(0);
		} else {

		}

	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Player getLeader() {
		return leader;
	}

	public void setLeader(Player leader) {
		this.leader = leader;
	}

	public List<Player> getMembers() {
		return members;
	}

	public void setMembers(List<Player> members) {
		this.members = members;
	}



	public int getLimit() {
		return limit;
	}



	public void setLimit(int limit) {
		this.limit = limit;
	}



	public boolean isLeader(Player player) {
		return leader.equals(player);
	}
	public List<Player> getInvites() {
		return invites;
	}
	public void setInvites(List<Player> invites) {
		this.invites = invites;
	}
	public boolean isMember(Player p) {
		return !isLeader(p) && getMembers().contains(p);
	}
}
