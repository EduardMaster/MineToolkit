package net.eduard.api.server.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.eduard.api.lib.storage.Storable;

/**
 * Controlador de Permiss§es e Grupos dos Jogadores
 * @author Eduard-PC
 *
 */
public class PermissionsManager implements Storable{
	
	private Map<String,  PermissionsGroup> groups = new HashMap<>();

	private Map<UUID, PermissionsPlayer> players = new HashMap<>();

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}

	public Map<String,  PermissionsGroup> getGroups() {
		return groups;
	}

	public void setGroups(Map<String,  PermissionsGroup> groups) {
		this.groups = groups;
	}

	public Map<UUID, PermissionsPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(Map<UUID, PermissionsPlayer> players) {
		this.players = players;
	}

}
