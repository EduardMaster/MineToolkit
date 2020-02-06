package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.server.kit.KitAbility;

public class Flash extends KitAbility {

	public int distance = 45;

	public Flash() {
		setIcon(Material.REDSTONE_TORCH_ON, "�fTeleporte para Longe");
		add(Material.REDSTONE_TORCH_ON);
		setTime(40);
		setClick(new PlayerClick(Material.REDSTONE_TORCH_ON, new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (cooldown(player)) {
						Mine.teleport(player, distance);
					}
				}
			}
		}));
	}
}
