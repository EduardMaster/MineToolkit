package net.eduard.api.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.game.Vip;
import net.eduard.api.setup.StorageAPI.Storable;

public class VipManager implements Storable{

	private Map<UUID, List<Vip>> vips = new HashMap<>();
	public void addVip(Player player, String vip, int days) {

	}
	public void removeVips(Player player) {

	}
	public void removeVip(Player player, String vip) {

	}
	public void increaseVip(Player player, String vip, int days) {

	}
	public void update(Player player) {

	}
	public boolean hasVip(Player player, String vip) {
		if (vips.containsKey(player.getUniqueId())) {

		}

		return false;
	}
	public Collection<List<Vip>> getVips() {
		return vips.values();
	}

	public void reduceVip(Player player, String vip, int days) {

	}
	public void setVips(Map<UUID, List<Vip>> vips) {
		this.vips = vips;
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
