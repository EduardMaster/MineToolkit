package net.eduard.api.server;

public class Systems {

	private static CashSystem cashSystem;
	private static SoulSystem soulSystem;
	private static VipSystem vipSystem;


	public static CashSystem getCashSystem() {
		return cashSystem;
	}

	public static void setCashSystem(CashSystem cashSystem) {
		Systems.cashSystem = cashSystem;
	}

	public static SoulSystem getSoulSystem() {
		return soulSystem;
	}

	public static void setSoulSystem(SoulSystem soulSystem) {
		Systems.soulSystem = soulSystem;
	}

	public static VipSystem getVipSystem() {
		return vipSystem;
	}

	public static void setVipSystem(VipSystem vipSystem) {
		Systems.vipSystem = vipSystem;
	}

}
