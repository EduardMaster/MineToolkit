package net.eduard.api.lib.old;

import java.util.Random;

import net.eduard.api.lib.modules.Extra;

/**
 * API de Aletoriedade
 * 
 * @version 1.0
 * @since 0.7
 * @deprecated Métodos adicionados na {@link Extra}
 * @author Eduard
 *
 */

public abstract interface RandomSetup {
	public default int getRandomInt(int minValue, int maxValue) {
		int min = Math.min(minValue, maxValue);
		int max = Math.max(minValue, maxValue);
		Random random = new Random();
		return min + random.nextInt(max - min + 1);
	}

	public default double getRandomDouble(int minValue, int maxValue) {
		double min = Math.min(minValue, maxValue);
		double max = Math.max(minValue, maxValue);
		Random random = new Random();
		return min + (max - min) * random.nextDouble();
	}

	public default boolean getChance(double chance) {
		return getRandomDouble() >= chance;
	}

	public default double getRandomDouble() {
		Random random = new Random();
		return random.nextDouble();
	}
}
