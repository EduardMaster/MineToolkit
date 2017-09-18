package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.game.Ability;

public class Forger extends Ability {

	public Forger() {
		setIcon(Material.COAL, "§fForge os itens!");
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if (hasKit(p)) {
//				API.broadcast("Current: ",e.getCurrentItem());
//				API.broadcast("Cursor: ",e.getCursor());
				if (e.getCursor() != null) {
					int amount = e.getCursor().getAmount();
					
					if (e.getCursor().getType() == Material.COAL) {
						if (e.getCurrentItem().getType() == Material.IRON_ORE){
							int oreAmount = e.getCurrentItem().getAmount();
							e.setCancelled(true);
							int result = amount;
							if (oreAmount>amount){
								e.getCurrentItem().setAmount(oreAmount-amount);
								e.setCursor(null);
							}else if (oreAmount == amount){
								e.setCurrentItem(null);
								e.setCursor(null);
							}else{
								e.getCurrentItem().setType(e.getCursor().getType());
								e.getCurrentItem().setAmount(amount-oreAmount);
								e.setCursor(null);
								result = amount -e.getCurrentItem().getAmount();
							}
							p.getInventory().addItem(new ItemStack(Material.IRON_INGOT,result));
						}
					}
				}
			}
		}
	}
}
