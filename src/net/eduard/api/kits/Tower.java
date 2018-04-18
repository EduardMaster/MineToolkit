package net.eduard.api.kits;

import org.bukkit.Material;

import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.KitType;


public class Tower extends Ability{

	public Tower() {
		setIcon(Material.DIAMOND_BOOTS, "§fJunte a força do Stomper com o Worm");
		add(KitType.STOMPER);
		add(KitType.WORM);
	}
	
	
}
