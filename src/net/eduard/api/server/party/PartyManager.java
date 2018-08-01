package net.eduard.api.server.party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class PartyManager {

	private List<Party> parties = new ArrayList<>();
	private List<Player> partyChat = new ArrayList<>();

	public Party createParty(Player leader) {
		Party party;
		parties.add(party = new Party(leader));
		return party;
	}
	public void deleteParty(Party party) {
		parties.remove(party);
	}
	
	public boolean isLeader(Player player) {
		for (Party party : parties) {
			if (party.getLeader().equals(player)) {
				return true;
			}
		}
		return false;
	}
	public boolean isMember(Player player) {
		for (Party party : parties) {
			if (party.getMembers().contains(player)) {
				return true;
			}
		}
		return false;
	}

	public List<Party> getParties() {
		return parties;
	}

	public void setParties(List<Party> parties) {
		this.parties = parties;
	}
	public List<Player> getPartyChat() {
		return partyChat;
	}
	public void setPartyChat(List<Player> partyChat) {
		this.partyChat = partyChat;
	}
	public Party getParty(Player player) {
		for (Party party  : parties) {
			if (party.getLeader().equals(player)) {
				return party;
			}
			if (party.getMembers().contains(player)) {
				return party;
			}
		}
		return null;
		
	}
	public Party getPartyByLeader(Player player) {
		return getParty(player);
	}

}
