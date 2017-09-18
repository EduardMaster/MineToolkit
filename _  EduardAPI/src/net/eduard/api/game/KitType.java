package net.eduard.api.game;

import net.eduard.api.setup.ExtraAPI;
import net.eduard.api.setup.RefAPI;

public enum KitType {

	DEFAULT, ASSASSIN, SCOUT, FORGER, CANNIBAL, CULTIVATOR, REAPER, ZEUS, INFERNOR, BOMBER, C4, RYU, FORCE_FIELD, SWITCHER, TELEPORTER, ARCHER, TURTLE, DESH_FIRE, PVP, BLINK, ACHILLES, URGAL, AJNIN, ANCHOR, BOXER, FIRE_MAN, FISHER_MAN, FLASH, GRAPPLER, HULK, KANGAROO, LUMBER_JACK, LUMBER_JACK_MEGA, MAGMA, MINER, MONK, NINJA, STOMPER, TANK, VACCUM, VIKING, VIPER, ENDER_MAGE, GLADIATOR, LAUNCHER, WORM, JACK_HAMMER, GRANDPA, TOWER, BUILD_TOWER, DEMO_MAN, SURPRISE, MAD_MAN, POSEIDON, SPEACIALIST, THOR, TIME_LORD, PHANTOM, SNAIL;

	private Ability ability;
	private KitType() {
		ability = getNewAbility();
		if (ability == null) {
			ExtraAPI.consoleMessage(
					"§bKitType §eFalha ao tentar ligar o Kit §f" + name());
		} else {

			ExtraAPI.consoleMessage("§bKitType §fKit §a" + name() +"§f carregado com sucesso!");

		}

	}
	public Ability getNewAbility() {
		try {
			return (Ability) RefAPI.getNew("#k" + ExtraAPI.toTitle(name(), ""));
		} catch (Exception e) {
			return null;
		}

	}
	public Ability getAbility() {
		return ability;
	}

}