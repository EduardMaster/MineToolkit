package net.eduard.api.tutorial;

import org.bukkit.entity.Player;

import net.eduard.api.util.ParticleEffect;

public class CriarParticle {

	public static void fazerEfeito(Player player) {
		ParticleEffect.displayBlockCrack(player.getLocation(), 1, (byte) 0, 0, 0, 0, 10);
	}
	public static void fazerEfeito2(Player player) {
		ParticleEffect.displayBlockCrack(player.getLocation(), 2, (byte) 0, 2, 3, 3, 10);
	}
	public static void fazerEfeito3(Player player) {
		ParticleEffect.ANGRY_VILLAGER.display(player.getLocation(), 1, 1, 1, 0.5F, 15);
	}
}
