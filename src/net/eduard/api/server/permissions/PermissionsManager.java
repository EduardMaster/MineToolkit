package net.eduard.api.server.permissions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.eduard.api.lib.storage.StorageAPI.Storable;

/**
 * Controlador de Permissões e Grupos dos Jogadores
 * @author Eduard-PC
 *
 */
public class PermissionsManager implements Storable{
	
	private Map<String,  PlayerGroup> groups = new HashMap<>();

	private Map<UUID, PlayerPermissions> players = new HashMap<>();

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}

	public Map<String,  PlayerGroup> getGroups() {
		return groups;
	}

	public void setGroups(Map<String,  PlayerGroup> groups) {
		this.groups = groups;
	}

	public Map<UUID, PlayerPermissions> getPlayers() {
		return players;
	}

	public void setPlayers(Map<UUID, PlayerPermissions> players) {
		this.players = players;
	}

}
