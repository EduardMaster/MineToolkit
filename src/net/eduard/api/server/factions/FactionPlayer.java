package net.eduard.api.server.factions;

import java.util.Map;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.StorageAttributes;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.server.ranks.Rank;
@StorageAttributes(indentificate=true)
public class FactionPlayer implements Storable {

	@StorageAttributes(reference=true)
	private FactionManager manager;
	@StorageAttributes(reference=true)
	private Faction faction;
	private OfflinePlayer playerData;
	private long lastLogin;
	private int kills;
	private int deaths;
	private int deathsByEnemies;
	private int deathsByNeutrals;
	private int deathsByCivilians;

	private transient Player player;

	public String getDisplayName() {
		StringBuilder b = new StringBuilder();
		if (hasFaction()) {
			b.append(getFaction().getPrefix() + " ");
		}
		b.append(getRank().getPrefix());
		b.append(getPlayerData().getName());
		return b.toString();
	}

	public Rank getRank() {
		return manager.getRank(this);
	}

	public FactionManager getManager() {
		return manager;
	}

	public void setManager(FactionManager manager) {
		this.manager = manager;
	}

	public int getDeathsByEnemies() {
		return deathsByEnemies;
	}

	public void setDeathsByEnemies(int deathsByEnemies) {
		this.deathsByEnemies = deathsByEnemies;
	}

	public int getDeathsByNeutrals() {
		return deathsByNeutrals;
	}

	public void setDeathsByNeutrals(int deathsByNeutrals) {
		this.deathsByNeutrals = deathsByNeutrals;
	}

	public int getDeathsByCivilians() {
		return deathsByCivilians;
	}

	public void setDeathsByCivilians(int deathsByCivilians) {
		this.deathsByCivilians = deathsByCivilians;
	}

	private double power;
	private double maxPower;

	public FactionPlayer() {
		// TODO Auto-generated constructor stub
	}

	public FactionPlayer(Player player) {
		setPlayerData(player);
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(double maxPower) {
		this.maxPower = maxPower;
	}

	public int getDeaths() {
		return deaths;
	}

	public Faction getFaction() {
		return faction;
	}

	public int getKills() {
		return kills;
	}

	public boolean hasFaction() {
		return this.faction != null;

	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void sendMessage(String message) {
		Player player = getPlayer();
		if (player != null)
			player.sendMessage(message);
	}

	public boolean isOnline() {
		return getPlayer() != null;
	}

	public Player getPlayer() {
		return player != null ? player : (player = getPlayerData().getPlayer());
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

	public double getKDR() {
		try {
			return kills / deaths;
		} catch (Exception e) {
			return 0D;
		}

	}

	public boolean isLeader() {
		return getFaction().getLeader().equals(this);
	}

	public OfflinePlayer getPlayerData() {
		return playerData;
	}

	public void setPlayerData(OfflinePlayer playerData) {
		this.playerData = playerData;
	}

	public String getName() {
		return getPlayerData().getName();
	}

	public UUID getId() {
		return getPlayerData().getUniqueId();
	}

	public long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public FactionRel getRel(FactionPlayer player) {
		return FactionRel.getRel(this, player);
	}

	public FactionRel getRel(FactionClaim claim) {
		return FactionRel.getRel(this, claim);
	}


	
}
