package net.eduard.api.kits;

import net.eduard.api.game.Ability;
import net.eduard.api.game.KitType;

public class None extends Ability{

	public None() {
		super("None",KitType.DEFAULT);
		setShowOnGui(false);
	}
}
