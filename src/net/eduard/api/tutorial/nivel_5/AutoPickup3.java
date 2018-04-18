package net.eduard.api.tutorial.nivel_5;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class AutoPickup3 implements Listener {

	@ EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getCursor().getType() == Material.AIR) {
			System.out.println(2);
			if (e.getRawSlot() >= e.getInventory().getSize()) {
				if (e.getInventory().getItem(15) == null) {
					e.getInventory().setItem(15, e.getCurrentItem());
					e.setCurrentItem(null);
					e.setCancelled(true);
				} else {
					ItemStack before = e.getCurrentItem();
					e.setCurrentItem(e.getInventory().getItem(15));
					e.getInventory().setItem(15, before);
					e.setCancelled(true);
				}
			} else if (e.getRawSlot() == 15) {
				e.getWhoClicked().getInventory().addItem(e.getCurrentItem());
				e.getInventory().setItem(15, null);
			}
		}
	}

}
