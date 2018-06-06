package net.eduard.api.lib.game;

public enum ParticleType {
	HUGE_EXPLOSION("hugeexplosion", 0), LARGE_EXPLODE("largeexplode", 1),
	FIREWORKS_SPARK("fireworksSpark", 2), BUBBLE("bubble", 3),
	SUSPEND("suspend", 4), DEPTH_SUSPEND("depthSuspend", 5),
	TOWN_AURA("townaura", 6), CRIT("crit", 7), MAGIC_CRIT("magicCrit", 8),
	MOB_SPELL("mobSpell", 9), MOB_SPELL_AMBIENT("mobSpellAmbient", 10),
	SPELL("spell", 11), INSTANT_SPELL("instantSpell", 12),
	WITCH_MAGIC("witchMagic", 13), NOTE("note", 14), PORTAL("portal", 15),
	ENCHANTMENT_TABLE("enchantmenttable", 16), EXPLODE("explode", 17),
	FLAME("flame", 18), LAVA("lava", 19), FOOTSTEP("footstep", 20),
	SPLASH("splash", 21), LARGE_SMOKE("largesmoke", 22), CLOUD("cloud", 23),
	RED_DUST("reddust", 24), SNOWBALL_POOF("snowballpoof", 25),
	DRIP_WATER("dripWater", 26), DRIP_LAVA("dripLava", 27),
	SNOW_SHOVEL("snowshovel", 28), SLIME("slime", 29), HEART("heart", 30),
	ANGRY_VILLAGER("angryVillager", 31), HAPPY_VILLAGER("happyVillager", 32);

private String particleName;

private ParticleType(String particleName, int id) {
	setParticleName(particleName);
}

public String getParticleName() {

	return particleName;
}

public void setParticleName(String particleName) {
	this.particleName = particleName;
}

}