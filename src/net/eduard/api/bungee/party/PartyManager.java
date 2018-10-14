package net.eduard.api.bungee.party;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

public class PartyManager {

	private ArrayList<Party> parties = new ArrayList<>();
	public Party getParty(String name) {
		for (Party party :parties) {
			if (party.getLeader().equalsIgnoreCase(name)) {
				return party;
			}
			if (party.getPlayers().contains(name)) {
				return party;
						
			}
		}
		return null;
	}
	public boolean isLeader(String name) {
		for (Party party : parties) {
			if (party.getLeader().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public Party createParty(String leader) {
		Party party = new Party();
		party.setLeader(leader);
		party.getPlayers().add(leader);
		parties.add(party);
		return party;

	}

	public boolean hasParty(String name) {
		for (Party party : parties) {
			if (party.getPlayers().contains(name)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Party> getParties() {
		return parties;
	}

	public void setParties(ArrayList<Party> parties) {
		this.parties = parties;
	}
}
