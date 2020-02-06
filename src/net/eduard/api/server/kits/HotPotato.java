package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.click.PlayerClickEntity;
import net.eduard.api.lib.click.PlayerClickEntityEffect;
import net.eduard.api.lib.game.Explosion;
import net.eduard.api.server.kit.KitAbility;
import org.bukkit.scheduler.BukkitRunnable;

public class HotPotato extends KitAbility {

	public int effectSeconds = 5;
	public HotPotato() {
		setIcon(Material.BAKED_POTATO, "");
		add(new ItemStack(Material.BAKED_POTATO));
		setTime(30);
		setExplosion( new Explosion(6, true, false));
		setMessage("§6");
		setClick(new PlayerClickEntity(Material.BAKED_POTATO, (player, entity, item) -> {
			// TODO Auto-generated method stub
			if (hasKit(player)) {
				if (entity instanceof Player) {
					Player target = (Player) entity;
					PlayerInventory inv = target.getInventory();
					inv.setHelmet(new ItemStack(Material.TNT));
					target.sendMessage(getMessage());
					new BukkitRunnable(){

						@Override
						public void run() {
							if (inv.getHelmet() != null) {
								if (inv.getHelmet()
										.getType() == Material.TNT) {
									getExplosion().create(target);
								}
							}
						}
					}.runTaskLater(getPlugin(),effectSeconds* 20);

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
