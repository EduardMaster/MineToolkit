package net.eduard.api.kits;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.API;
import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;
import net.eduard.api.setup.ItemAPI;

public class Phantom extends Ability {
	public int effectSeconds = 5;
	public Phantom() {
		setIcon(Material.FEATHER, "§fVoe por 5 segundos");
		add(Material.FEATHER);
		setTime(40);
		message("§6Acabou o tempo nao pode mais voar");
		setClick(new PlayerClick(Material.FEATHER,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {

					if (cooldown(player)) {
						ItemAPI.saveArmours(player);
						ItemAPI.setEquip(player, Color.WHITE, "§b" + getName());
						player.setAllowFlight(true);
						API.TIME.delay(effectSeconds*20,new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (hasKit(player)) {
									ItemAPI.getArmours(player);
									sendMessage(player);
								}
								player.setAllowFlight(false);
							}
						});

					}
				}
			}
		}));
	}

}
