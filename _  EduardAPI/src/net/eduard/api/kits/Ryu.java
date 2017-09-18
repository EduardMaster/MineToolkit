package net.eduard.api.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;

public class Ryu extends Ability {

	public static List<Snowball> inEffect = new ArrayList<>();

	public int damage = 8;

	public Ryu() {
		setIcon(Material.BEACON, "§fAtire seu Haduken");
		add(Material.DIAMOND_BLOCK);
		setClick(new PlayerClick(Material.DIAMOND_BLOCK,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (cooldown(player)) {

						Location location = player.getEyeLocation();
						BlockIterator blocksToAdd = new BlockIterator(location,
								0.0D, 40);
						while (blocksToAdd.hasNext()) {
							Location blockToAdd = blocksToAdd.next()
									.getLocation();
							player.getWorld().playEffect(blockToAdd,
									Effect.STEP_SOUND, Material.DIAMOND_BLOCK,
									20);
							player.playSound(blockToAdd, Sound.IRONGOLEM_THROW, 3.0F,
									3.0F);
						}
						Snowball project = player.launchProjectile(Snowball.class);
						inEffect.add(project);
						project.setVelocity(player.getLocation().getDirection()
								.normalize().multiply(10));
					}
				}

			}
		}));
	}
	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Snowball) {
			Snowball snowball = (Snowball) e.getDamager();
			if (inEffect.contains(snowball)) {
				e.setDamage(e.getDamage() + damage);
				inEffect.remove(snowball);
			}

		}
	}

}
