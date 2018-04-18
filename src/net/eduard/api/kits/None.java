package net.eduard.api.kits;

import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.KitType;

public class None extends Ability{

	public None() {
		super("None",KitType.DEFAULT);
		setShowOnGui(false);
	}
}
