package net.eduard.api.server;

public class Systems {

	private static CashSystem cashSystem;
	private static SoulSystem soulSystem;
	private static VipSystem vipSystem;
	private static ScoreSystem scoreSystem;
	private static PartySystem partySystem;

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

	public static ScoreSystem getScoreSystem() {
		return scoreSystem;
	}

	public static void setScoreSystem(ScoreSystem scoreSystem) {
		Systems.scoreSystem = scoreSystem;
	}

	public static PartySystem getPartySystem() {
		return partySystem;
	}

	public static void setPartySystem(PartySystem partySystem) {
		Systems.partySystem = partySystem;
	}

}
