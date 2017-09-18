package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;
import net.eduard.api.setup.GameAPI;

public class Vaccum extends Ability {
	public int range = 20;

	public Vaccum() {
		setIcon(Material.EYE_OF_ENDER, "§fSugue seus inimigos até você");
		add(Material.EYE_OF_ENDER);
		setClick(
				new PlayerClick(Material.EYE_OF_ENDER, new PlayerClickEffect() {

					@Override
					public void onClick(Player player, Block block,
							ItemStack item) {
						if (hasKit(player)) {
							if (cooldown(player)) {
								for (Entity entity : player.getNearbyEntities(
										range, range - 5, range)) {
									if (entity instanceof LivingEntity) {
										LivingEntity livingEntity = (LivingEntity) entity;
										GameAPI.moveTo(livingEntity,
												player.getLocation(), -0.2);
									}
								}
							}

						}

					}
				}));
	}

}
