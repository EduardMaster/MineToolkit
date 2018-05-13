package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;

public class Grandpa extends Ability{

	public Grandpa() {
		setIcon(Material.STICK, "§fJogue seus inimigos para longe");
		Mine.addEnchant(add(Material.STICK), Enchantment.KNOCKBACK, 2);
	}
}
