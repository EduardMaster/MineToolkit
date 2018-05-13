package net.eduard.api.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.game.Ability;

public class Miner extends Ability {

	public Miner() {
		setIcon(Material.STONE_PICKAXE, "§fMinere muito rapido");
		ItemStack item = new ItemStack(Material.STONE_PICKAXE);
		Mine.addEnchant(item, Enchantment.DURABILITY, 2);
		Mine.addEnchant(item, Enchantment.DIG_SPEED, 2);
		add(item);
	}

	public void check(Location loc,ItemStack item) {
		int range = 1;
		int  high = 1;
		int X = loc.getBlock().getX();
		int Y = loc.getBlock().getY();
		int Z = loc.getBlock().getZ();
		for (int x = X-range;x<=X+range;x++) {
			for (int z = Z-range;z<=Z+range;z++) {
				for (int y = Y;y<=Y+high;y++) {
					loc = new Location(loc.getWorld(), x, y, z);
					Material type = loc.getBlock().getType();
					if (type!=Material.AIR&(!type.name().contains("LOG"))) {
						loc.getBlock().breakNaturally(item);
					}
					
				}
			}
		}
	}
	@EventHandler
	public void event(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (Mine.isUsing(p, "PICKAXE")) {
				check(e.getBlock().getLocation(),p.getItemInHand());
			}
		}

	}
}
