package net.eduard.api.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.game.Rank;
import net.eduard.api.setup.ScoreAPI.FakeOfflinePlayer;
import net.eduard.api.setup.StorageAPI.Storable;
import net.eduard.api.setup.VaultAPI;

/**
 * Controlador de Ranks
 * @author Eduard
 *
 */
public class RankManager implements Storable{

	private NavigableMap<String, Rank> ranks = new TreeMap<>();
	private Map<UUID, Rank> players = new HashMap<>();
	public void updatePermissions() {
		for (Entry<String, Rank> map : ranks.entrySet()) {
			Rank rank = map.getValue();
			rank.updatePermissions();
		}
	}
	public void updateGroups()
	{
		for (Entry<String, Rank> map : ranks.entrySet()) {
			Rank rank = map.getValue();
			for (Player p : API.getPlayers()) {
				VaultAPI.getPermission().playerRemoveGroup(p, rank.getName());
			}
		}
		for (Entry<UUID, Rank> map: players.entrySet()) {
			Rank rank = map.getValue();
			UUID id = map.getKey();
			FakeOfflinePlayer fake = new FakeOfflinePlayer(null,id);
			VaultAPI.getPermission().playerAddGroup(null,fake, rank.getName());
		}
	}
	
	public NavigableMap<String, Rank> getRanks() {
		return ranks;
	}
	public void setRanks(NavigableMap<String, Rank> ranks) {
		this.ranks = ranks;
	}

	public Rank getLastRank() {
		return ranks.lastEntry().getValue();
	}
	public Rank getFirstRank() {
		return ranks.firstEntry().getValue();
	}
	public Rank getRank(Player player) {
		return players.get(player.getUniqueId());
	}

	public Map<UUID, Rank> getPlayers() {
		return players;
	}
	public void setPlayers(Map<UUID, Rank> players) {
		this.players = players;
	}

	public void rankUp(Player p) {

	}
	public void rankDown(Player p) {

	}
	public boolean canRankUp(Player p) {
		return false;

	}

	public int getPercent(Player p) {
		return 0;
	}

	public Rank getRank(String name) {
		return ranks.get(name.toLowerCase());
	}
	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub
		
	}

}
