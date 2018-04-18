package net.eduard.api.server.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.eduard.api.setup.StorageAPI.Reference;

public class PlayerPermissions {
	
	private UUID playerId;
	
	private String playerName;

	private List<String> permissions = new ArrayList<>();
	@Reference
	private List<PlayerGroup> groups = new ArrayList<>();
	
	
	public List<PlayerGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<PlayerGroup> groups) {
		this.groups = groups;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	public UUID getPlayerId() {
		return playerId;
	}
	public void setPlayerId(UUID playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
}
