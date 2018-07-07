package net.eduard.api.server.vips;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.eduard.api.lib.VaultAPI;
import net.eduard.api.lib.modules.FakePlayer;
import net.eduard.api.lib.storage.Storable;

public class VipPlayer extends FakePlayer implements Storable{

	public VipPlayer(String name, UUID id) {
		super(name, id);
	}
	public VipPlayer() {
		super("Player");
		// TODO Auto-generated constructor stub
	}
	private List<VipUsed> vips = new ArrayList<>();
	private List<VipUsed> history = new ArrayList<>();
	public List<VipUsed> getVips() {
		return vips;
	}
	public void setVips(List<VipUsed> vips) {
		this.vips = vips;
	}
	public List<VipUsed> getHistory() {
		return history;
	}
	public void setHistory(List<VipUsed> history) {
		this.history = history;
	}
	public boolean hasVips() {
		return !vips.isEmpty();
	}
	public boolean hasVip(String vipName) {
		return getVip(vipName)!= null;
	}
	public void removeVip(VipUsed vip) {
	
		VaultAPI.getPermission().playerRemoveGroup(null, this, vip.getVipName());
		vips.remove(vip);
	}
	public void removeVip(String vipName) {
		removeVip(getVip(vipName));
		
	}
	public VipUsed getVip(String vipName) {
		for (VipUsed vip : vips) {
			if (vip.getVipName().equalsIgnoreCase(vipName)) {
				return vip;
			}
		}
		return null;
	}
	
	
}
