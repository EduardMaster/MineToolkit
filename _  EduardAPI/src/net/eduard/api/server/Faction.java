package net.eduard.api.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.setup.StorageAPI.Storable;

public class Faction implements Storable{

	private transient FactionManager manager;
	private String name;
	private String prefix;
	private String suffix;
	private Location base;
	private FactionMember leader;
	private List<FactionClaim> claims = new ArrayList<>();
	private List<FactionMember> members = new ArrayList<>();
	private List<FactionMember> invites = new ArrayList<>();
	private List<Faction> allies = new ArrayList<>();
	private List<Faction> rivals = new ArrayList<>();
	public Faction(String name, String prefix) {
		this.name = name;
		this.prefix = prefix;
	}
	public List<FactionClaim> getClaims() {
		return claims;
	}
	public void sendMessage(String message) {
		for (FactionMember member : getMembers()) {
			member.sendMessage(message);
		}
	}
	public void send(CommandSender sender, String message) {
		API.chat(sender,
				message.replace("$tag", prefix).replace("$name", name));
	}

	public List<FactionMember> getInvites() {
		return invites;
	}



	public FactionMember getLeader() {
		return leader;
	}
	public List<FactionMember> getMembers() {
		return members;
	}
	public String getName() {
		return name;
	}
	public String getPrefix() {
		return prefix;
	}
	public String getSuffix() {
		return suffix;
	}

	public void setClaims(List<FactionClaim> claims) {
		this.claims = claims;
	}


	public void setInvites(List<FactionMember> invites) {
		this.invites = invites;
	}

	public void setLeader(FactionMember leader) {
		this.leader = leader;
	}

	public void setMembers(List<FactionMember> members) {
		this.members = members;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public FactionRel getRel(FactionClaim claim) {
		Faction faction = claim.getFaction();
		if (faction == null) {
			return FactionRel.FREE_ZONE;
		}
		if (equals(faction)) {
			if (claim.isOnAttack()) {
				return FactionRel.WAR;
			}
			return FactionRel.MEMBER;
		}
		return getRel(claim.getFaction());

	}
	public FactionRel getRel(Faction faction) {
		if (faction == null) {
			return FactionRel.FREE_ZONE;
		}
		if (getAllies().contains(faction)) {
			return FactionRel.ALLY;
		}
		if (getRivals().contains(faction)) {
			return FactionRel.RIVAL;
		}
		if (faction.equals(manager.getWarZone())) {
			return FactionRel.WAR_ZONE;
		}
		if (faction.equals(manager.getProtectedZone())) {
			return FactionRel.PROTECTED_ZONE;
		}
		return FactionRel.NEUTRAL;

	}

	public List<Faction> getRivals() {
		return rivals;
	}

	public void setRivals(List<Faction> rivals) {
		this.rivals = rivals;
	}

	public List<Faction> getAllies() {
		return allies;
	}

	public void setAllies(List<Faction> allies) {
		this.allies = allies;
	}

	public boolean hasBase() {
		return getBase() != null;
	}
	public boolean isOnAttack() {
		for (FactionClaim claim : claims) {
			if (claim.isOnAttack()) {
				return true;
			}
		}
		return false;
	}
	public double getKillPerDeath() {
		int kill = 0;
		int death = 0;
		for (FactionMember member : members) {
			kill+=member.getKills();
			death+= member.getDeaths();
		}
		if (kill==0|death==0	) {
			return 0;
		}
		return kill/death;
	}
	public Location getBase() {
		return base;
	}
	public void setBase(Location base) {
		this.base = base;
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
