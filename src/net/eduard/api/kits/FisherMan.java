package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Ability;

public class FisherMan extends Ability {

	public FisherMan() {
		setIcon(Material.FISHING_ROD, "§fPuxe seus inimigos ate voce");
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
