package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.API;
import net.eduard.api.game.Ability;
import net.eduard.api.game.Sounds;
import net.eduard.api.setup.ItemAPI;

public class Vitality extends Ability {
	public ItemStack soup = API.newItem(Material.BROWN_MUSHROOM, "§6Sopa");
	
	public Vitality() {
		setIcon(Material.MUSHROOM_SOUP, "§fAo eliminar um Inimigo vai ganhar sopas");
		sound(Sounds.create(Sound.LEVEL_UP));
	}

	@EventHandler
	public void event(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getKiller();
			if (p == null) {
				return;
			}
			ItemAPI.fill(p.getInventory(), soup);
			if (hasKit(p)) {
				makeSound(p);
			}
			p.sendMessage(getMessage());
		}
	}
}
