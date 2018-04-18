package net.eduard.api.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Ability;

public class Switcher extends Ability {
	public Switcher() {
		setIcon(Material.SNOW_BALL,
				"§fAo acertar uma Bolinha de Neve você sera teleportado ate ela!");

		add(new ItemStack(Material.SNOW_BALL, 10));

	}
	@Override
	@EventHandler
	public void event(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Snowball) {

			Snowball snowball = (Snowball) e.getDamager();
			if (snowball.getShooter() instanceof Player) {
				Player p = (Player) snowball.getShooter();
				if (hasKit(p)) {
					Location loc = snowball.getLocation();
					Location ploc = p.getLocation();
					Mine.teleport(p, loc);
					Mine.teleport(e.getEntity(), ploc);
				}

			}

		}
	}

}
