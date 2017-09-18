package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.API;
import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;
import net.eduard.api.setup.ItemAPI;


public class Backpacker extends Ability{
	
	public ItemStack soup = API.newItem(Material.BROWN_MUSHROOM, "§6Sopa");
	
	public Backpacker() {
		setIcon(Material.LAPIS_BLOCK, "§fGanhe uma espada Melhor");
		add(Material.WOOD_SWORD);
		setClick(new PlayerClick(Material.NETHER_STAR, new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				if (hasKit(player)){
					if (cooldown(player)){
						Inventory inv = API.newInventory("§7Backpacker", 6*9);
						ItemAPI.fill(inv, soup);
						player.openInventory(inv);
					}
				}
			}
		}));
			
	}

}
