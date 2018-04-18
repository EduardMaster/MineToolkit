package net.eduard.api.tutorial.nivel_4;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.setup.Mine;

public class TeleportDelay implements Listener {

	public int delaySeconds = 3;
	@EventHandler
	public void event(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		if (!teleporting.contains(p)) {
//			if (!p.hasPermission("delay.bypass")) {
				e.setCancelled(true);
				teleporting.add(p);
				new BukkitRunnable() {

					@Override
					public void run() {
						p.teleport(e.getTo());
						teleporting.remove(p);
					}
				}.runTaskLater(Mine.getMainPlugin(), delaySeconds*20);
//			}
		}
	}

	public static List<Player> teleporting = new ArrayList<>();

}
