package net.eduard.api.test;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.modules.Animation;

public class TesteAnimacaoArmostand extends EventsManager {

	@EventHandler
	public void clicar(PlayerInteractEvent e) {
		Player p = e.getPlayer();

		if (p.getItemInHand() == null)
			return;
		if (p.getItemInHand().getType() == Material.DIAMOND) {

			ArmorStand stand = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
			stand.setSmall(true);
			stand.setVisible(true);
			stand.setGravity(false);
			stand.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
			Animation animador = new Animation(stand);
			new BukkitRunnable() {
				int duracao = 100;

				@Override
				public void run() {

					duracao--;
					animador.moveHeadUp(10);
					if (duracao == 0) {
						cancel();
						stand.remove();
					}
				}
			}.runTaskTimer(getPlugin(), 2, 2);

		}

	}
}
