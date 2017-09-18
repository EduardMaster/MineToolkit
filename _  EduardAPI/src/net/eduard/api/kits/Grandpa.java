package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import net.eduard.api.game.Ability;
import net.eduard.api.setup.ItemAPI;

public class Grandpa extends Ability{

	public Grandpa() {
		setIcon(Material.STICK, "§fJogue seus inimigos para longe");
		ItemAPI.addEnchant(add(Material.STICK), Enchantment.KNOCKBACK, 2);
	}
}
