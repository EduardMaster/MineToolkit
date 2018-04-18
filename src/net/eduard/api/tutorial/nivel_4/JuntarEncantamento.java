package net.eduard.api.tutorial.nivel_4;

import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class JuntarEncantamento implements Listener{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void enchantItem(InventoryClickEvent e) {
		if (e.getWhoClicked() instanceof Player) {
			if (e.getCursor() != null) {
				if (e.getCursor().getType() == Material.ENCHANTED_BOOK) {
					if (e.getCurrentItem() == null)
						return;
					if (e.getCurrentItem().getType() == Material.AIR)
						return;
					if (e.getCurrentItem()
							.getType() == Material.ENCHANTED_BOOK) {
						EnchantmentStorageMeta a = (EnchantmentStorageMeta) e
								.getCursor().getItemMeta();
						EnchantmentStorageMeta b = (EnchantmentStorageMeta) e
								.getCurrentItem().getItemMeta();
						for (Entry<Enchantment, Integer> enchant : a
								.getStoredEnchants().entrySet()) {
							b.addStoredEnchant(enchant.getKey(),
									enchant.getValue(), true);
						}

						ItemStack clone = e.getCurrentItem();
						clone.setItemMeta(b);
						e.setCurrentItem(clone);
						e.setCursor(null);
						e.setCancelled(true);
					} else {
						EnchantmentStorageMeta a = (EnchantmentStorageMeta) e
								.getCursor().getItemMeta();
						ItemStack clone = e.getCurrentItem();
						clone.addUnsafeEnchantments(a.getStoredEnchants());
						e.setCurrentItem(clone);
						e.setCursor(null);
						e.setCancelled(true);
					}

				}
			}
		}
	}
}
