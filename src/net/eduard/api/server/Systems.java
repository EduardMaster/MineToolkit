package net.eduard.api.server;

public class Systems {

	private static RankSystem rankSystem;
	private static CashSystem cashSystem;
	private static CoinSystem coinSystem;
	private static SoulSystem soulSystem;
	private static VipSystem vipSystem;
	private static ScoreSystem scoreSystem;
	private static PartySystem partySystem;
	private static GeneratorSystem generatorSystem;
	

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

	public static CoinSystem getCoinSystem() {
		return coinSystem;
	}

	public static void setCoinSystem(CoinSystem coinSystem) {
		Systems.coinSystem = coinSystem;
	}

	public static GeneratorSystem getGeneratorSystem() {
		return generatorSystem;
	}

	public static void setGeneratorSystem(GeneratorSystem generatorSystem) {
		Systems.generatorSystem = generatorSystem;
	}

	public static RankSystem getRankSystem() {
		return rankSystem;
	}

	public static void setRankSystem(RankSystem rankSystem) {
		Systems.rankSystem = rankSystem;
	}
}
