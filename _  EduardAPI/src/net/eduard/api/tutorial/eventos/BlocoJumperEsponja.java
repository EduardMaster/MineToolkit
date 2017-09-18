package net.eduard.api.tutorial.eventos;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import net.eduard.api.manager.TimeManager;

public class BlocoJumperEsponja extends TimeManager{
	@EventHandler
	public void BlocoPulador(EntityDamageEvent e) {

		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)
				&& JUMPERS.contains(p)) {
				e.setCancelled(true);
				JUMPERS.remove(p);
			}
		
		}
	}


	public static final ArrayList<Player> JUMPERS = new ArrayList<>();
	@EventHandler
	public void BlocoPulador(PlayerMoveEvent e) {

		Player p = e.getPlayer();
		if (e.getTo().getBlock().getRelative(BlockFace.DOWN)
			.getType() == Material.DIAMOND_BLOCK) {
			JUMPERS.remove(p);
			Vector sponge = p.getLocation().getDirection().multiply(0).setY(1);
			p.setVelocity(sponge);
			JUMPERS.add(p);
		}
	}
}
