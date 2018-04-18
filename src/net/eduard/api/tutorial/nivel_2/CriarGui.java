package net.eduard.api.tutorial.nivel_2;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CriarGui implements Listener{

	@EventHandler 
	public void event(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getItem() == null)return;
		if (e.getItem().getType() == Material.COMPASS) {
			abriGui1(p);
		}
	}
	@EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getCurrentItem()==null)return;
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (e.getInventory().getTitle().equals("§6Kits")) {
				if (e.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
					p.chat("/kit pvp");
					p.closeInventory();
				}
				if (e.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
					abriGui2(p);
				}
				e.setCancelled(true);
			}
		}
	}
	public static void abriGui1(Player p) {
		Inventory inv = Bukkit.createInventory(null, 6*9,"§6Kits");;
		inv.addItem(new ItemStack(Material.DIAMOND_SWORD));
		p.openInventory(inv);
	}
	public static void abriGui2(Player p) {
		Inventory inv = Bukkit.createInventory(null, 6*9,"§6Kits Vips");;
		inv.addItem(new ItemStack(Material.GOLD_SWORD));
		p.openInventory(inv);
	}
}
