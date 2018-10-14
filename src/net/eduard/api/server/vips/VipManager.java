	package net.eduard.api.server.vips;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.Storable;

public class VipManager implements Storable {

	private List<Vip> vips = new ArrayList<>();
	private List<VipKey> keys = new ArrayList<>();
	private List<VipUser> users = new ArrayList<>();

	public VipUser getUser(Player player) {

		for (VipUser user : users) {
			if (user.getUniqueId().equals(player.getUniqueId())) {
				return user;
			}
		}
		VipUser user = new VipUser(player.getName(),player.getUniqueId());
		users.add(user);
		return user;
	}
	public void addVip(VipUser user,Vip vip,long duration) {
		if (user.hasVip(vip.getName())) {
			VipUsed vipUsed = user.getVip(vip.getName());
			vipUsed.setDuration(vipUsed.getDuration()+duration);
		}else {
			VipUsed vipUsed = new VipUsed();
			vipUsed.setActive(true);
			vipUsed.setDuration(duration);
			vipUsed.setSince(Extra.getNow());
			vipUsed.setVipName(vip.getName());
			user.getVips().add(vipUsed);
			for (String cmd : vip.getCommands()) {
				Mine.makeCommand(cmd.replaceFirst("/", "").replace("$player", user.getName()));
			}
			VaultAPI.getPermission().playerAddGroup(null, user.getPlayer(),vip.getName());
		}
	}
	public void removeVip(VipUser user,Vip vip) {
		user.removeVip(vip.getName());
	}

	public VipUser getUser(String playerName) {

		for (VipUser user : users) {
			if (user.getName().equals(playerName)) {
				return user;
			}
		}
		VipUser user = new VipUser();
		user.setName(playerName);
		user.setIdByName();
		users.add(user);
		return user;
	}
	public boolean hasVip(String vipName) {
		
		return getVip(vipName)!=null;
	}
	public VipUser getUser(UUID playerId) {

		for (VipUser user : users) {
			if (user.getUniqueId().equals(playerId)) {
				return user;
			}
		}
		return null;
	}
	public Vip getVip(String name) {
		for (Vip vip : vips) {
			if (vip.getName().equalsIgnoreCase(name)) {
				return vip;
			}
		}
		return null;
	}

	public List<VipUser> getUsers() {
		return users;
	}

	public void setUsers(List<VipUser> users) {
		this.users = users;
	}

	public List<Vip> getVips() {
		return vips;
	}

	public void setVips(List<Vip> vips) {
		this.vips = vips;
	}

	public List<VipKey> getKeys() {
		return keys;
	}

	public void setKeys(List<VipKey> keys) {
		this.keys = keys;
	}

}
