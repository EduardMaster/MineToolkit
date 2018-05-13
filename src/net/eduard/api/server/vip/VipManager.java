package net.eduard.api.server.vip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.lib.VaultAPI;
import net.eduard.api.lib.storage.StorageAPI.Storable;

public class VipManager implements Storable{

	private Map<UUID, List<Vip>> playersVips = new HashMap<>();
	public List<Vip> getVips(Player player){
		List<Vip> vips = playersVips.get(player.getUniqueId());
		if (vips == null) {
			vips = new ArrayList<>();
			playersVips.put(player.getUniqueId(), vips);
		}
		
		
		return vips;
		
	}
	public void addVip(Player player, String vip, int days) {
		List<Vip> vips = getVips(player);
		Vip gameVip = new Vip();
		gameVip.setName(vip);
		gameVip.setVipStart(System.currentTimeMillis());
		gameVip.setDays(days);
		vips.add(gameVip);
	
		
//		vips.put(player.getUniqueId(), value)
		
	
	}
	public void removeVips(Player player) {
		List<Vip> vips = getVips(player);
		for (Vip vip : vips) {
			VaultAPI.getPermission().playerRemoveGroup(player, vip.getName());
		}
	}
	public void removeVip(Player player, String vipName) {
		List<Vip> vips = getVips(player);
		for (Vip vip :vips) {
			if (vip.getName().equalsIgnoreCase(vipName)) {
				vip.removeVip();
			}
		}
	}
	public void increaseVip(Player player, String vip, int days) {

	}
	public void update(Player player) {

	}
	public boolean hasVip(Player player, String vipName) {
		for (Vip vip : getVips(player)) {
			if (vip.getName().equalsIgnoreCase(vipName)) {
				return true;
			}
		}
		
		return false;
	}

	public void reduceVip(Player player, String vip, int days) {

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
