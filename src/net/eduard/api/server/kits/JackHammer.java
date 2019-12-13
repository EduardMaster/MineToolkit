package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.click.ClickComparationType;
import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.server.kit.KitAbility;

public class JackHammer extends KitAbility{
	
	public JackHammer() {
		setIcon(Material.STONE_AXE, "�fCrie grandes buracos na terra");
		add(Material.STONE_AXE);
		setTime(20);
		setTimes(8);
		PlayerClick clickEffect = new PlayerClick(Material.STONE_AXE,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
//					e.setCancelled(false);
					if (cooldown(player)) {
						block.setType(Material.AIR);
						double y = block.getY();
						while(y>2) {
							block = block.getRelative(BlockFace.DOWN);
							block.setType(Material.AIR);
							y--;
						}
						
					}
					
				}
				
			}
		});
		setClick(clickEffect);
		clickEffect.setComparationType(ClickComparationType.ON_BLOCK);
	}
}
