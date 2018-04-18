package net.eduard.api.tutorial.nivel_2;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Mine;

public class CriarLoja implements Listener {
	@EventHandler
	public void event(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getMaterial() == Material.DIAMOND) {
			abriMenu(p);
		}
	}

	private void abriMenu(Player p) {
		Inventory inv = Mine.newInventory("texto", 9);
		inv.setItem(3, new ItemStack(Material.DIAMOND,15));
		inv.setItem(5, new ItemStack(Material.DIAMOND,16));
		p.openInventory(inv);
	}
	@EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Inventory inv = e.getInventory();
			if (inv.getTitle().equals("texto")) {
				if (e.getCurrentItem() == null)
					return;
				if (inv.containsAtLeast(new ItemStack(Material.DIAMOND), 16)) {
					Mine.broadcast("OI");
				}
				if (e.getCurrentItem().getType() == Material.DIAMOND) {
					Mine.remove(inv, new ItemStack(Material.DIAMOND,3));;
					e.setCancelled(true);
				}

			}
		}
	}

}
