package net.eduard.api.server;

public enum FactionRel  {

	RIVAL("§c"), ALLY("§9"), NEUTRAL("§7"), MEMBER("§a"), FREE_ZONE(
			"§d"), PROTECTED_ZONE(
					"§6"), WAR_ZONE("§4"), WAR("§d"), LEADER("§6");

	private FactionRel(String color) {
		setColor(color);
	}

	private String color;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
