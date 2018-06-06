package net.eduard.api.server.vips;

public class VipAPI {

	private static VipManager vipManager;

	public static VipManager getVipManager() {
		return vipManager;
	}

	public static void setVipManager(VipManager vipManager) {
		VipAPI.vipManager = vipManager;
	}
}
