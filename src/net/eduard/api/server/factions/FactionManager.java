package net.eduard.api.server.factions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.game.Chunk;
import net.eduard.api.lib.storage.Reference;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.server.ranks.Rank;
import net.eduard.api.server.ranks.RankManager;

public class FactionManager implements Storable {

	private int maxPlayerSize=15;
	private int startingPower=5,startingPowerMax=5;
	private ItemStack itemMaxPower,itemInstantPower;
	private Map<EntityType, Double> generatorsPrices = new HashMap<>();
	private Map<String, Faction> factions = new HashMap<>();
	private Map<UUID, FactionPlayer> members = new HashMap<>();
	private transient FactionGeneratorManager generatorManager;
	private RankManager ranks = new RankManager();
	@Reference
	private Faction warZone;
	@Reference
	private Faction protectedZone;
	@Reference
	private Faction freeZone;


	public Rank getRank(FactionPlayer player) {
		return ranks.getPlayerRank(player.getPlayerData());
	}

	public FactionManager() {
		warZone = new Faction("Zona de Guerra", "§4Zona de Guerra");
		protectedZone = new Faction("Zona Protegida", "§6Zona Protegida!");
		freeZone = new Faction("Zona Livre", "§2Zona Livre");
		factions.put("zonadeguerra", warZone);
		factions.put("zonaprotegida", protectedZone);
		factions.put("zonalivre", freeZone);
		warZone.setManager(this);
		protectedZone.setManager(this);
		freeZone.setManager(this);

		Rank rankLeader = new Rank("leader", 4);
		
		rankLeader.setPrefix("#");
		rankLeader.setHeadName("Líder");
		rankLeader.setPreviousRank("captain");
		ranks.getRanks().put("leader", rankLeader);

		Rank rankMember = new Rank("member", 2);
		rankMember.setPrefix("+");
		rankMember.setHeadName("Membro");
	
		rankMember.setNextRank("captain");
		ranks.getRanks().put("member", rankMember);
		
		Rank rankRecruit = new Rank("recruit", 1);
		rankRecruit.setPrefix("-");
		rankRecruit.setNextRank("member");
		rankRecruit.setHeadName("Recruta");

		ranks.getRanks().put("recruit", rankRecruit);
		
		Rank rankCaptain = new Rank("captain", 3);
		rankCaptain.setPrefix("*");
		rankCaptain.setNextRank("leader");
		rankCaptain.setHeadName("Capitão");
	
		ranks.getRanks().put("captain", rankCaptain);
		
		
		ranks.setFirst("recruit");
		ranks.setLast("leader");
		for (EntityType e : EntityType.values()) {
			if (e.isAlive()) {
				if (e.isSpawnable()) {
					generatorsPrices.put(e, 1000D);
				}
			}
		}
	}

	public Faction getFaction(Player player) {
		return getMember(player).getFaction();
	}

	public Map<String, Faction> getFactions() {
		return factions;
	}

	public void addGenerator(Location location, EntityType type) {
		FactionClaim claim = getClaim(location);
		Faction fac = claim.getFaction();
		fac.getGenerators().put(type, fac.getGenerators().getOrDefault(type, 0) + 1);
	}

	public void removeGenerator(Location location, EntityType type) {
		FactionClaim claim = getClaim(location);
		Faction fac = claim.getFaction();
		fac.getGenerators().put(type, fac.getGenerators().getOrDefault(type, 0) - 1);
	}

	public FactionPlayer getMember(String nome) {
		for (FactionPlayer member : members.values()) {
			if (member.getPlayerData().getName().equalsIgnoreCase(nome)) {
				return member;

			}
		}

		return null;
	}

	public FactionPlayer getMember(OfflinePlayer player) {
		FactionPlayer member = members.get(player.getUniqueId());
		// Mine.console("§c"+ranks.getFirstRank());
		// Mine.console("§e"+ranks.getLastRank());
		if (member == null) {
			member = new FactionPlayer();
			member.setPlayerData(player);
			member.setManager(this);
			// ranks.setRank(player.getUniqueId(), ranks.getFirst());
			members.put(player.getUniqueId(), member);
		}
		return member;

	}

	public FactionPlayer getMember(Player player) {
		FactionPlayer member = members.get(player.getUniqueId());
		// Mine.console("§c"+ranks.getFirst());
		// Mine.console("§e"+ranks.getRanks());
		if (member == null) {
			member = new FactionPlayer(player);
			member.setManager(this);
			// ranks.setRank(player.getUniqueId(), ranks.getFirst());
			members.put(player.getUniqueId(), member);
		}
		return member;

	}

	// public FactionPlayer getPlayer(Player player) {
	// return members.get(player.getUniqueId());
	// }
	public Map<UUID, FactionPlayer> getMembers() {
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
				if (loopClaim.getChunk().equals(claim.getChunk())) {
					return loopClaim;
				}

			}
		}
		return claim;
	}

	public void factionCreate(FactionPlayer player, Faction faction) {
		faction.setLeader(player);
		ranks.setLastRank(player.getPlayerData());
		factionJoin(player, faction);
	}

	public FactionClaim getClaim(Location location) {
		return getClaim(new Chunk(location.getChunk()));
	}

	public boolean isClaimed(Location location) {
		return getClaim(location).getFaction() != null;
	}

	public void factionJoin(FactionPlayer member, Faction faction) {
		faction.getMembers().add(member);
		member.setFaction(faction);
	}

	public void factionLeave(Player player) {
		FactionPlayer member = getMember(player);
		Faction fac = member.getFaction();
		member.getFaction().getMembers().remove(member);
		member.setFaction(null);
		ranks.setFirstRank(player);
		if (fac.getLeader().equals(member)) {
			deleteFaction(fac);
		}
	}

	public void factionClaim(Player player) {
		Faction fac = getFaction(player);
		FactionClaim claim = new FactionClaim(player.getLocation());
		claim.setFaction(fac);
		fac.getClaims().add(claim);
	}

	public boolean canClaim(Chunk chunk, Faction faction) {
		boolean temOutraFac = false;
		boolean temTerrenoPerto = false;
		int range = 1;
		for (int x = -range; x <= range; x++) {
			for (int z = -range; z <= range; z++) {
				Chunk newChunk = chunk.newChunk(chunk.getX() + x, chunk.getZ() + z);
				FactionClaim claim = getClaim(newChunk);
				if (!faction.equals(claim.getFaction()) && claim.isDomined()) {
					temOutraFac = true;
				}
				if (faction.equals(claim.getFaction())) {
					temTerrenoPerto = true;
				}

			}
		}
		return (temTerrenoPerto && !temOutraFac);
	}

	public Faction createFaction(String name, String tag) {
		Faction faction = new Faction(name, tag);
		factions.put(name.toLowerCase(), faction);
		faction.setManager(this);
		return faction;
	}

	public void deleteFaction(Faction faction) {

		for (FactionPlayer member : faction.getMembers()) {
			member.setFaction(null);
			ranks.setFirstRank(member.getPlayerData());

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

	public void setMembers(Map<UUID, FactionPlayer> members) {
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

	public FactionGeneratorManager getGeneratorManager() {
		return generatorManager;
	}

	public void setGeneratorManager(FactionGeneratorManager generatorManager) {
		this.generatorManager = generatorManager;
	}

	public Map<EntityType, Double> getGeneratorsPrices() {
		return generatorsPrices;
	}

	public void setGeneratorsPrices(Map<EntityType, Double> generatorsPrices) {
		this.generatorsPrices = generatorsPrices;
	}

	public ItemStack getItemInstantPower() {
		return itemInstantPower;
	}

	public void setItemInstantPower(ItemStack itemInstantPower) {
		this.itemInstantPower = itemInstantPower;
	}

	public ItemStack getItemMaxPower() {
		return itemMaxPower;
	}

	public void setItemMaxPower(ItemStack itemMaxPower) {
		this.itemMaxPower = itemMaxPower;
	}

	public int getMaxPlayerSize() {
		return maxPlayerSize;
	}

	public void setMaxPlayerSize(int maxPlayerSize) {
		this.maxPlayerSize = maxPlayerSize;
	}

	public int getStartingPower() {
		return startingPower;
	}

	public void setStartingPower(int startingPower) {
		this.startingPower = startingPower;
	}

	public int getStartingPowerMax() {
		return startingPowerMax;
	}

	public void setStartingPowerMax(int startingPowerMax) {
		this.startingPowerMax = startingPowerMax;
	}

}
