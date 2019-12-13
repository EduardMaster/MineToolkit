package net.eduard.api.server.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.server.kit.KitAbility;

public class BuildTower extends KitAbility {
	public int size = 65;
	public Material type = Material.DIRT;

	public BuildTower() {
		setIcon(Material.DIRT, "�fContrua uma Torre de Terra");
		add(Material.CARPET);
		setClick(new PlayerClick(Material.CARPET,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				if (hasKit(player)) {
					if (cooldown(player)) {
						Location loc = player.getLocation();
						for (int id = 1; id <= size; id++) {
							loc.add(0	, 1, 0);
							loc.getBlock().setType(type);
						}
						loc.add(0, 1, 0);
						Mine.teleport(player, loc);
					}
				}
			}
		}));
		setTime(30);
	}
}
