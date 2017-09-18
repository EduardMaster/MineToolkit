package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.eduard.api.game.Ability;


public class Urgal extends Ability{

	public Urgal() {
		setIcon(Material.POTION,8261, "§fGanhe poções que te dão muita força");
		add(new Potion(PotionType.STRENGTH,1).toItemStack(2));
	}
}
