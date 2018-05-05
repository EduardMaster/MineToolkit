package net.eduard.api.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.eduard.api.setup.click.PlayerClickEntity;
import net.eduard.api.setup.click.PlayerClickEntityEffect;
import net.eduard.api.setup.game.Ability;

public class Monk extends Ability {

	public Monk() {
		setIcon(Material.BLAZE_ROD, "§fBagunce o inventario do Inimigo");
		add(Material.BLAZE_ROD);
		setTime(15);
		setClick(new PlayerClickEntity(Material.BLAZE_ROD,new PlayerClickEntityEffect() {
			
			@Override
			public void onClickAtEntity(Player player, Entity entity, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						if (cooldown(player)) {
							PlayerInventory inv = target.getInventory();
							ItemStack test = target.getItemInHand();
							int value = new Random().nextInt(36);
							ItemStack replaced = inv.getItem(value);
							inv.setItemInHand(replaced);
							inv.setItem(value, test);
						}
					}

				}
			}
		}));
	}
}
