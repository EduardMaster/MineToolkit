package net.eduard.api.lib.storage.game;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.Mine;

public enum KitType {

	DEFAULT, ASSASSIN, SCOUT, FORGER, CANNIBAL, CULTIVATOR, REAPER, ZEUS, INFERNOR, BOMBER, C4, RYU, FORCE_FIELD, SWITCHER, TELEPORTER, ARCHER, TURTLE, DESH_FIRE, PVP, BLINK, ACHILLES, URGAL, AJNIN, ANCHOR, BOXER, FIRE_MAN, FISHER_MAN, FLASH, GRAPPLER, HULK, KANGAROO, LUMBER_JACK, LUMBER_JACK_MEGA, MAGMA, MINER, MONK, NINJA, STOMPER, TANK, VACCUM, VIKING, VIPER, ENDER_MAGE, GLADIATOR, LAUNCHER, WORM, JACK_HAMMER, GRANDPA, TOWER, BUILD_TOWER, DEMO_MAN, SURPRISE, MAD_MAN, POSEIDON, SPEACIALIST, THOR, TIME_LORD, PHANTOM, SNAIL, MILK;

	private Ability ability;
	private KitType() {
		ability = getNewAbility();
		if (ability == null) {
			Mine.console(
					"�bKitType �eFalha ao tentar ligar o Kit �f" + name());
		} else {

			Mine.console("�bKitType �fKit �a" + name() +"�f carregado com sucesso!");

		}

	}
	public Ability getNewAbility() {
		try {
			return (Ability) Extra.getNew("#k" + Mine.toTitle(name(), ""));
		} catch (Exception e) {
			return null;
		}

	}
	public Ability getAbility() {
		return ability;
	}

}