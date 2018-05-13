package net.eduard.api.kits;

import net.eduard.api.lib.storage.game.Ability;
import net.eduard.api.lib.storage.game.KitType;

public class None extends Ability{

	public None() {
		super("None",KitType.DEFAULT);
		setShowOnGui(false);
	}
}
