package net.eduard.api.server.kits;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.lib.game.VisualEffect;
import net.eduard.api.lib.game.Jump;
import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.server.kit.KitAbility;

public class DeshFire extends KitAbility {
	public static ArrayList<Player> inEffect = new ArrayList<>();
	public int range = 5;
	public int effectSeconds = 5;

	public DeshFire() {
		setIcon(Material.REDSTONE_BLOCK, "�fGanha um boost para frente");
		setTime(40);
		add(Material.REDSTONE_BLOCK);
		setDisplay(new VisualEffect(Effect.MOBSPAWNER_FLAMES, 10));
		setJump(new Jump(true, 0.5, 2, SoundEffect.create("CLICK")));
		setClick(new PlayerClick(Material.REDSTONE_BLOCK,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (cooldown(player)) {
						Mine.saveArmours(player);
						Mine.setEquip(player, Color.RED, "�c" + getName());
						inEffect.add(player);
						player.setAllowFlight(true);
						asyncDelay(new Runnable() {

							@Override
							public void run() {
								if (hasKit(player)) {
									Mine.getArmours(player);
									inEffect.remove(player);
								}
							}
						}, effectSeconds);
						getJump().create(player);
					}

				}
			}
		}));
	}
	@EventHandler
	public void event(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (inEffect.contains(p)) {
			for (Entity entity : p.getNearbyEntities(range, range, range)) {
				if (entity instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.setFireTicks(20*effectSeconds);
					getDisplay().create(livingEntity.getLocation());

				}
			}
		}

	}

}
