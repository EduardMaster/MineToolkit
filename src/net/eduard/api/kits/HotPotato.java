package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.click.PlayerClickEntity;
import net.eduard.api.setup.click.PlayerClickEntityEffect;
import net.eduard.api.setup.game.Ability;
import net.eduard.api.setup.game.Explosion;

public class HotPotato extends Ability {

	public int effectSeconds = 5;
	public HotPotato() {
		setIcon(Material.BAKED_POTATO, "");
		add(new ItemStack(Material.BAKED_POTATO));
		setTime(30);
		explosion( new Explosion(6, true, false));
		message("§6");
		setClick(new PlayerClickEntity(Material.BAKED_POTATO,new PlayerClickEntityEffect() {
			
			@Override
			public void onClickAtEntity(Player player, Entity entity, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (entity instanceof Player) {
						Player target = (Player) entity;
						PlayerInventory inv = target.getInventory();
						inv.setHelmet(new ItemStack(Material.TNT));
						sendMessage(target);
						Mine.TIME.delay( effectSeconds,new Runnable() {

							@Override
							public void run() {
								if (inv.getHelmet() != null) {
									if (inv.getHelmet()
											.getType() == Material.TNT) {
										makeExplosion(target);
									}
								}

							}
						});
					}
				}
			}
		}));

	}
	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) e.getDamager();
			if (arrow.getShooter() instanceof Player) {
				Player p = (Player) arrow.getShooter();
				if (hasKit(p)) {
					p.getInventory().addItem(new ItemStack(Material.ARROW));
				}

			}

		}
	}

}
