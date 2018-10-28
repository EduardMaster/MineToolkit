package net.eduard.api.server.permissions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.eduard.api.lib.storage.StorageAttributes;

public class PermissionsPlayer {
	
	private UUID playerId;
	
	private String playerName;

	private List<String> permissions = new ArrayList<>();
	@StorageAttributes
	private List<PermissionsGroup> groups = new ArrayList<>();
	
	
	public List<PermissionsGroup> getGroups() {
		return groups;
	}
	public void setGroups(List<PermissionsGroup> groups) {
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
