package net.eduard.api.server.factions;

public enum FactionRel {

	RIVAL("§c", true), ALLY("§a"), NEUTRAL("§f"), MEMBER("§8"), FREE_ZONE("§7",
			true), PROTECTED_ZONE("§6"), WAR_ZONE("§4", true), WAR("§d", true), LEADER("§6");

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
