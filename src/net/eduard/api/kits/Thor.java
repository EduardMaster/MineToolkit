package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.click.PlayerClick;
import net.eduard.api.lib.storage.click.PlayerClickEffect;
import net.eduard.api.lib.storage.game.Ability;

public class Thor extends Ability {
	public double damage = 4;

	public Thor() {
		setIcon(Material.WOOD_AXE, "§fSolte raios nos inimigos");
		add(Material.WOOD_AXE);
		setClick(new PlayerClick(Material.WOOD_AXE,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (cooldown(player)) {
						Mine.strike(player, 100);
					}
				}
			}
		}));
		setTime(7);
	}
	@EventHandler
	public void event(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.LIGHTNING) {
				if (hasKit(p)) {
					e.setCancelled(true);
				} else {
					e.setDamage(damage);
				}
				
			}

		}
	}

}
