package net.eduard.api.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.click.PlayerClick;
import net.eduard.api.click.PlayerClickEffect;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.Explosion;
import net.eduard.api.setup.game.Jump;
import net.eduard.api.setup.game.Sounds;

public class C4 extends Ability {

	
	public Material materialType = Material.TNT;
	public static HashMap<Player, Item> bombs = new HashMap<>();

	public C4() {
		setIcon(Material.TNT, "§fPlante e Ative a C4");
		add(Material.STONE_BUTTON);
		message("§6A bomba foi plantada!");
		jump(new Jump(false, 0.6, 0.5,
				Sounds.create("CLICK")));
		explosion(new Explosion(4, false, false));
		setTime(2);
		setTimes(2);
		setClick(new PlayerClick(Material.STONE_BUTTON,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				if (hasKit(player)) {
					if (cooldown(player)) {
						if (bombs.containsKey(player)) {
							Item c4 = bombs.get(player);
							makeExplosion(c4);
							c4.remove();
							bombs.remove(player);
						} else {
							Item c4 = player.getWorld().dropItemNaturally(
									player.getEyeLocation(), new ItemStack(materialType));
							c4.setPickupDelay(99999);
							Mine.setDirection(c4, player);
							jump(c4);
							bombs.put(player, c4);
							sendMessage(player);
						}
					}
				}
			}
		}));
	}

}
