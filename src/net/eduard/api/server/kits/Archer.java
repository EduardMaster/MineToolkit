package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.kit.KitAbility;

public class Archer extends KitAbility {
	public Archer() {
		setIcon(Material.BOW,
				"§fGanhe uma flecha ao você acertar ao player a flecha será teleportado até seu inventario.");
		add(Mine.addEnchant(new ItemStack(Material.BOW),
				Enchantment.ARROW_DAMAGE, 1));
		add(new ItemStack(Material.ARROW, 10));

	}

	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player p = (Player) arrow.getShooter();
				if (hasKit(p)) {
					p.getInventory().addItem(new ItemStack(Material.ARROW));
				}

			}

		}
	}

}
