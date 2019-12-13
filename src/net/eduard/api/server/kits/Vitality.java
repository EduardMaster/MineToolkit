package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.server.kit.KitAbility;

public class Vitality extends KitAbility {
	public ItemStack soup = Mine.newItem(Material.BROWN_MUSHROOM, "�6Sopa");
	
	public Vitality() {
		setIcon(Material.MUSHROOM_SOUP, "�fAo eliminar um Inimigo vai ganhar sopas");
		setSound(SoundEffect.create("LEVEL_UP"));
	}

	@EventHandler
	public void event(EntityDeathEvent e) {
		if (e.getEntity().getKiller() instanceof Player) {
			Player p = e.getEntity().getKiller();
			if (p == null) {
				return;
			}
			Mine.fill(p.getInventory(), soup);
			if (hasKit(p)) {
				getSound().create(p);
			}
			p.sendMessage(getMessage());
		}
	}
}
