package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import net.eduard.api.setup.click.PlayerClickEntity;
import net.eduard.api.setup.click.PlayerClickEntityEffect;
import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.Jump;
import net.eduard.api.setup.game.Sounds;

public class Hulk extends Ability {

	public Hulk() {
		setIcon(Material.DISPENSER, "§fLevante seus inimigos");
		setTime(15);
		jump(new Jump(Sounds.create("BURP"),
				new Vector(0, 2, 0)));
		setClick(new PlayerClickEntity(Material.AIR,new PlayerClickEntityEffect() {
			
			@Override
			public void onClickAtEntity(Player player, Entity entity, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (player.getPassenger() != null) {

						if (entity.equals(player.getPassenger())) {
							Entity target = entity;
							player.eject();
							jump(target);;
							player.sendMessage("§6Voce jogou alguem para Cima!");

						}
					} else {
						if (cooldown(player)) {
							player.setPassenger(entity);
							player.sendMessage(
									"§6Voce colocou alguem em suas costas!");
						}
					}

				}

			}
		}));
	}

}
