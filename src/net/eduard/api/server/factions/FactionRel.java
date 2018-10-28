package net.eduard.api.server.factions;

public enum FactionRel {

	RIVAL("§c", true), ALLY("§a"), NEUTRAL("§f"), MEMBER("§8"), FREE_ZONE("§7",
			true), PROTECTED_ZONE("§6"), WAR_ZONE("§4", true), WAR("§d", true), LEADER("§6");

	public static FactionRel getRel(Faction fac, Faction faction) {
		if (faction == null) {
			return FREE_ZONE;
		}
		if (faction.isFreeZone()) {
			return FREE_ZONE;
		}
		if (faction.isWarZone()) {
			return WAR_ZONE;
		}
		if (faction.isSafeZone()) {
			return FactionRel.PROTECTED_ZONE;
		}

		if (fac == null) {
			return NEUTRAL;
		}
		if (fac.isFreeZone()) {
			return FREE_ZONE;
		}
		if (fac.isWarZone()) {
			return WAR_ZONE;
		}
		if (fac.isSafeZone()) {
			return FactionRel.PROTECTED_ZONE;
		}
		if (fac.equals(faction)) {
			return FactionRel.MEMBER;
		}
		if (fac.getAllies().contains(faction) || faction.getAllies().contains(fac)) {
			return FactionRel.ALLY;
		}
		if (fac.getRivals().contains(faction) || faction.getRivals().contains(fac)) {
			return FactionRel.RIVAL;
		}

		return FactionRel.NEUTRAL;
	}

	public static FactionRel getRel(FactionPlayer player, FactionClaim claim) {
		if (player == null || claim == null) {
			return FactionRel.FREE_ZONE;
		}
		if (claim.isOnAttack()) {
			return FactionRel.WAR;
		}
		return getRel(player.getFaction(), claim.getFaction());
	}

	public static FactionRel getRel(FactionPlayer player, FactionPlayer player2) {
		if (player == null || player2 == null) {
			return FactionRel.NEUTRAL;
		}
		if (player.hasFaction() && player2.hasFaction()) {
			return getRel(player.getFaction(), player2.getFaction());
		}
		return NEUTRAL;
	}

	private FactionRel(String color) {
		setColor(color);
	}

	private FactionRel(String color, boolean pvp) {
		setColor(color);
		setPvP(pvp);
	}

	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	private boolean pvp;

	public void setPvP(boolean can) {
		this.pvp = can;
	}

	public boolean canPvP() {
		return pvp;
	}

}
