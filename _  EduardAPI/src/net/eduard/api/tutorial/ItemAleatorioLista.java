package net.eduard.api.tutorial;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

public class ItemAleatorioLista implements Listener{
	@EventHandler
	public void ItemRandom(ProjectileHitEvent e) {

		if (e.getEntity() instanceof Snowball) {
			Snowball s = (Snowball) e.getEntity();
			ItemStack dima = new ItemStack(Material.DIAMOND_SWORD);
			ItemStack ferro = new ItemStack(Material.IRON_SWORD);
			s.getWorld().dropItem(s.getLocation(), getRandomItem(dima, ferro));
		}
	}

	public ItemStack getRandomItem(ItemStack... items) {

		return items[new Random().nextInt(items.length - 1)];
	}
}
