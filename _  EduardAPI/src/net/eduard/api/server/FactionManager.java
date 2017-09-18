package net.eduard.api.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.eduard.api.game.Rank;
import net.eduard.api.manager.RankManager;
import net.eduard.api.setup.StorageAPI.Storable;

public class FactionManager implements Storable {

	private Map<String, Faction> factions = new HashMap<>();
	private Map<UUID, FactionMember> members = new HashMap<>();
	private RankManager ranks = new RankManager();
	private Faction warZone;
	private Faction protectedZone;
	private Faction freeZone;
	public FactionManager() {
		warZone = new Faction("Zona de Guerra", "§4Zona de Guerra");
		protectedZone = new Faction("Zona Protegida", "§6Zona Protegida!");
		freeZone = new Faction("Zona Livre", "§2Zona Livre");
		Rank leader = new Rank("lider", 1);
		leader.setPrefix("**");
		ranks.getRanks().put(leader.getName(), leader);
		Rank member = new Rank("membro", 2);
		member.setPrefix("+");
		ranks.getRanks().put(member.getName(), member);

	}

	public Faction getFaction(Player player) {
		return getMember(player).getFaction();
	}

	public Map<String, Faction> getFactions() {
		return factions;
	}
	public FactionMember getMember(Player player) {
		FactionMember member = members.get(player.getUniqueId());
		if (member == null) {
			member = new FactionMember();
			members.put(player.getUniqueId(), member);
		}
		return member;

	}
	public FactionMember getPlayer(Player player) {
		return members.get(player.getUniqueId());
	}
	public Map<UUID, FactionMember> getMembers() {
		return members;
	}
	public boolean hasFaction(Player player) {
		return getMember(player).getFaction() != null;
	}
	public Faction getFaction(String name) {
		return factions.get(name.toLowerCase());
	}
	public boolean hasFaction(String name) {
		return factions.containsKey(name.toLowerCase());
	}
	public Faction getFaction(String name, String prefix) {
		Faction fac = getFaction(name);
		if (fac == null) {
			for (Faction faction : factions.values()) {
				if (faction.getPrefix().equalsIgnoreCase(prefix)) {
					return faction;
				}
			}
		}
		return fac;
	}
	public boolean hasFaction(String name, String prefix) {
		return getFaction(name, prefix) != null;
	}

	public FactionClaim getClaim(Chunk chunk) {
		FactionClaim claim = new FactionClaim(chunk);
		for (Faction fac : factions.values()) {
			for (FactionClaim loopClaim : fac.getClaims()) {
				if (loopClaim.equals(claim)) {
					return loopClaim;
				}

			}
		}
		return claim;
	}
	public FactionClaim getClaim(Location location) {
		return getClaim(location.getChunk());
	}
	public boolean isClaimed(Location location) {
		return getClaim(location).getFaction() != null;
	}
	public void factionJoin(FactionMember member, Faction faction) {
		faction.getMembers().add(member);
		member.setFaction(faction);
	}
	public void factionLeave(Player player) {
		FactionMember member = getMember(player);
		member.getFaction().getMembers().remove(member);
		member.setFaction(null);
		member.setRank(ranks.getFirstRank());
	}
	public void factionClaim(Player player) {
		Faction fac = getFaction(player);
		FactionClaim claim = new FactionClaim(player.getLocation());
		claim.setFaction(fac);
		fac.getClaims().add(claim);
	}

	public Faction createFaction(String name, String tag) {
		Faction faction = new Faction(name, tag);
		factions.put(name.toLowerCase(), faction);
		return faction;
	}
	public void deleteFaction(Faction faction) {

		for (FactionMember member : faction.getMembers()) {
			member.setFaction(null);
			member.setRank(ranks.getFirstRank());
		}
		for (Faction fac : faction.getAllies()) {
			fac.getAllies().remove(faction);
		}
		for (Faction fac : faction.getRivals()) {
			fac.getRivals().remove(faction);
		}
		factions.remove(faction.getName().toLowerCase());
	}

	public void setFactions(Map<String, Faction> factions) {
		this.factions = factions;
	}

	public void setMembers(Map<UUID, FactionMember> members) {
		this.members = members;
	}

	public Faction getProtectedZone() {
		return protectedZone;
	}

	public void setProtectedZone(Faction protectedZone) {
		this.protectedZone = protectedZone;
	}

	public Faction getWarZone() {
		return warZone;
	}

	public void setWarZone(Faction warZone) {
		this.warZone = warZone;
	}

	public Faction getFreeZone() {
		return freeZone;
	}

	public void setFreeZone(Faction freeZone) {
		this.freeZone = freeZone;
	}

	public RankManager getRanks() {
		return ranks;
	}

	public void setRanks(RankManager ranks) {
		this.ranks = ranks;
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
