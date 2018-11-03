package net.eduard.api.server.ranks;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.modules.Copyable.NotCopyable;
import net.eduard.api.lib.storage.StorageAttributes;
import net.eduard.api.lib.storage.Storable;

/**
 * Controlador de Ranks
 * 
 * @author Eduard
 *
 */
@StorageAttributes(indentificate=true)
public class RankManager implements Storable {
	
	private static RankManager instance;
	
	@NotCopyable
	private transient Map<UUID, PermissionAttachment> permissions = new HashMap<>();
	private String first;
	private String last;
	private boolean perGroup = true;
	private Map<String, Rank> ranks = new LinkedHashMap<>();
	
	@StorageAttributes(reference=true)
	private Map<OfflinePlayer, Rank> playersRanks = new HashMap<>();

	public PermissionAttachment getPermissionControler(OfflinePlayer player) {
		PermissionAttachment controler = permissions.get(player.getUniqueId());
		if (controler== null) {
			Player p = player.getPlayer();
			if (p!=null) {
				controler = new PermissionAttachment(Mine.getMainPlugin(), p);
				permissions.put(p.getUniqueId(), controler);
			}
				
		}
		
		return controler;
	}

	

	public void promote(OfflinePlayer player) {
		setRank(player, getPlayerRank(player).getNextRank());

	}

	public void removePermissions(OfflinePlayer player) {
		Rank rank = getPlayerRank(player);
		if (perGroup) {
			VaultAPI.getPermission().playerRemoveGroup(null, player, rank.getName());
		} else {
			PermissionAttachment controler = getPermissionControler(player);
			for (String permission : rank.getPermissions()) {
				controler.unsetPermission(permission);
			}
		}

	}

	public void addPermissions(OfflinePlayer player) {
		Rank rank = getPlayerRank(player);
		
		if (perGroup) {
			VaultAPI.getPermission().playerAddGroup(null, player, rank.getName());
		} else {
			PermissionAttachment controler = getPermissionControler(player);
			for (String permission : rank.getPermissions()) {
				controler.setPermission(permission, true);
			}
		}

	}


	public boolean isInLastRank(OfflinePlayer p) {
		return getLastRank().equals(getPlayerRank(p));
	}

	public void demote(OfflinePlayer player) {
		setRank(player, getPlayerRank(player).getPreviousRank());
	}

	public void updateGroupPermissions() {
		for (Rank rank : ranks.values()) {
			for (String permission : rank.getPermissions()) {
				VaultAPI.getPermission().groupRemove((World) null, rank.getName(), permission);
			}
			for (String permission : rank.getPermissions()) {
				VaultAPI.getPermission().groupAdd((World) null, rank.getName(), permission);
			}

		}
	}

	public Rank getFirstRank() {
		return getRank(first);
	}

	public Rank getLastRank() {
		return getRank(last);
	}



	public Rank getPlayerRank(OfflinePlayer p) {
		FakePlayer fake = new FakePlayer(p);
		Rank rank = playersRanks.get(fake);

		if (rank == null) {
			playersRanks.put(fake, rank = getFirstRank());
			addPermissions(fake);
		}
		
		return rank;
	}

	public void rankUp(OfflinePlayer p) {
		Rank rank = getPlayerRank(p);
		VaultAPI.getEconomy().withdrawPlayer(p, rank.getPrice());
		setRank(p, rank.getNextRank());
	}



	public void setRank(OfflinePlayer p, String newRank) {
		setRank(p, getRank(newRank));

	}public void setRank(OfflinePlayer p, Rank newRank) {
		FakePlayer fake = new FakePlayer(p);
		removePermissions(fake);
		playersRanks.put(fake, newRank);
		addPermissions(fake);

	}

	public boolean canRankUp(OfflinePlayer p) {
		return VaultAPI.getEconomy().has(p, getPlayerRank(p).getPrice());

	}

	public double getPercent(OfflinePlayer p) {

		Rank rank = getPlayerRank(p);
		double dinheiroParaUpar = rank.getPrice();
		double dinheiroAtual = VaultAPI.getEconomy().getBalance(p);
		if (dinheiroAtual < 1 || dinheiroParaUpar < 1) {
			return 0;
		}
		double resultado = dinheiroAtual / dinheiroParaUpar;

		return resultado > 1 ? 1 : resultado;
	}

	public boolean existsRank(String name) {
		return getRank(name) != null;
	}

	public Rank getRank(String name) {
		for (Entry<String, Rank> entry : ranks.entrySet()) {
			String rankName = entry.getKey();
			Rank rank = entry.getValue();
			if (fix(name).equals(fix(rankName))) {
				return rank;
			}
			if (fix(name).equals(fix(rank.getName()))) {
				return rank;
			}

		}
		return null;
	}

	public static String fix(String text) {
		return text.replace(" ", "").trim().toLowerCase();
	}

	public RankManager() {
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

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public boolean isPerGroup() {
		return perGroup;
	}

	public void setPerGroup(boolean perGroup) {
		this.perGroup = perGroup;
	}



	public Map<UUID, PermissionAttachment> getPermissions() {
		return permissions;
	}



	public void setPermissions(Map<UUID, PermissionAttachment> permissions) {
		this.permissions = permissions;
	}



	public Map<String, Rank> getRanks() {
		return ranks;
	}



	public void setRanks(Map<String, Rank> ranks) {
		this.ranks = ranks;
	}



	public Map<OfflinePlayer, Rank> getPlayersRanks() {
		return playersRanks;
	}



	public void setPlayersRanks(Map<OfflinePlayer, Rank> playersRanks) {
		this.playersRanks = playersRanks;
	}



	public void setLastRank(OfflinePlayer player) {
		setRank(player, getLast());
	}
	public void setFirstRank(OfflinePlayer player) {
		setRank(player, getFirst());
	}



	public static RankManager getInstance() {
		return instance;
	}



	public static void setInstance(RankManager instance) {
		RankManager.instance = instance;
	}

}
