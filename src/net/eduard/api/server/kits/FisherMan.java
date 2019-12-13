package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import net.eduard.api.lib.Mine;
import net.eduard.api.server.kit.KitAbility;

public class FisherMan extends KitAbility {

	public FisherMan() {
		setIcon(Material.FISHING_ROD, "�fPuxe seus inimigos ate voce");
		add(Material.FISHING_ROD);
	}

	@EventHandler
	private void event(PlayerFishEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (e.getCaught() != null) {
				Mine.teleport(e.getCaught(), p.getLocation());
			}
		}
	}
}
