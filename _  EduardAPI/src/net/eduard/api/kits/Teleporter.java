package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.game.Ability;
import net.eduard.api.setup.GameAPI;
import net.eduard.api.setup.ItemAPI;

public class Teleporter extends Ability {
	public Teleporter() {
		setIcon(Material.BOW, "§fAo acertar a flecha você é teleportado até ela!");
		add(ItemAPI.addEnchant(new ItemStack(Material.BOW), Enchantment.ARROW_DAMAGE, 1));
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
					GameAPI.teleport(p, arrow.getLocation());
				}

			}

		}
	}

}
