package net.eduard.api.lib.game;

import java.util.Map;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;

@StorageAttributes(inline = true)
public class Calculator implements Storable {
	private double base = 10;
	private double perLevel = 5;
	private double limit = 0;
	private double variationPerLevel = 0;
	private double variation = 0;

	public Calculator() {

	}

	public Calculator(double base, double perLevel) {
		super();
		this.perLevel = perLevel;
		this.base = base;
	}

	public Calculator(double base, double perLevel, double limit) {
		super();
		this.limit = limit;
		this.perLevel = perLevel;
		this.base = base;
	}

	public double getValue(double level) {
		double result = (perLevel * (level)) + base;
		double variationNumber = variation + (variationPerLevel * level);
		double random = Extra.getRandomDouble(-variationNumber, variationNumber);
		double resultFinal = result + random; // + extra;
		if (limit > 0 && resultFinal > limit) {
			return limit;
		}
		if (resultFinal <= 0) {
			resultFinal = 0;
		}
		return resultFinal;
	}

	public double getBase() {
		return base;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getPerLevel() {
		return perLevel;
	}

	public void setPerLevel(double perLevel) {
		this.perLevel = perLevel;
	}

//	public double getVariationBonus() {
//		return variationBonus;
//	}
//
//	public void setVariationBonus(double variationBonus) {
//		this.variationBonus = variationBonus;
//	}

	public double getLimit() {
		return limit;
	}

	public void setLimit(double limit) {
		this.limit = limit;
	}

	public double getVariationPerLevel() {
		return variationPerLevel;
	}

	public void setVariationPerLevel(double variationPerLevel) {
		this.variationPerLevel = variationPerLevel;
	}

	public double getVariation() {
		return variation;
	}

	public void setVariation(double variation) {
		this.variation = variation;
	}

//	public double getVariationOnLevels() {
//		return variationOnLevels;
//	}
//
//	public void setVariationOnLevels(double variationOnLevels) {
//		this.variationOnLevels = variationOnLevels;
//	}
}