package net.eduard.api.server.factions;

import org.bukkit.Location;

import net.eduard.api.lib.game.Chunk;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI.Reference;

public class FactionClaim implements Storable {

	public boolean saveInline() {
		return true;
	}

	@Reference
	private Faction faction;

	private boolean onAttack;

	private Chunk chunk;

	public FactionClaim() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chunk == null) ? 0 : chunk.hashCode());
		result = prime * result + ((faction == null) ? 0 : faction.hashCode());
		result = prime * result + (onAttack ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FactionClaim other = (FactionClaim) obj;
		if (chunk == null) {
			if (other.chunk != null)
				return false;
		} else if (!chunk.equals(other.chunk))
			return false;
		if (faction == null) {
			if (other.faction != null)
				return false;
		} else if (!faction.equals(other.faction))
			return false;
		if (onAttack != other.onAttack)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FactionClaim [faction=" + faction + ", onAttack=" + onAttack + ", chunk=" + chunk + "]";
	}

	public FactionClaim(Chunk chunk) {
		setChunk(chunk);
	}
	public void claimBy(Faction faction) {
		if (isDomined()) {
			this.faction.getClaims().remove(this);
		}
		this.faction = faction;
		faction.getClaims().add(this);
	}

	public FactionClaim(Location location) {
		this(new Chunk(location.getChunk()));
	}

	public boolean isDomined() {
		return faction != null;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public boolean isOnAttack() {
		return onAttack;
	}

	public void setOnAttack(boolean onAttack) {
		this.onAttack = onAttack;
	}

	public Chunk getChunk() {
		return chunk;
	}

	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}


}
