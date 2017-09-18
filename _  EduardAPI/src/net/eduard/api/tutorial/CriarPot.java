package net.eduard.api.tutorial;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class CriarPot {

	public static void darPocao(Player p) {
		@SuppressWarnings("deprecation")
		ItemStack item = new Potion(PotionType.FIRE_RESISTANCE, 1,false,true).toItemStack(5);
		p.getInventory().addItem(item);
	}
}
