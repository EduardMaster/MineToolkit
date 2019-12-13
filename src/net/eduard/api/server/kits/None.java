package net.eduard.api.server.kits;

import net.eduard.api.lib.modules.KitType;
import net.eduard.api.server.kit.KitAbility;

public class None extends KitAbility{

	public None() {
		super("None",KitType.DEFAULT);
		setShowOnGui(false);
	}
}
