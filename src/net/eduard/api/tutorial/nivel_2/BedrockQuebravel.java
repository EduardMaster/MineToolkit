package net.eduard.api.tutorial.nivel_2;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class BedrockQuebravel implements  Listener{
	@EventHandler
	public void event(BlockDamageEvent e) {
		if (e.getBlock().getType() == Material.BEDROCK) {
			Player p = e.getPlayer();
			if (p.getItemInHand()!=null) {
				if (p.getItemInHand().getType().name().contains("PICKAXE")){
					if (p.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
						e.setInstaBreak(true);
					}
				}
			}
		}
	}

}
