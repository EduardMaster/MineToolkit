package net.eduard.api.tutorial.nivel_2;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class CriarGravidade implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void quandoCriarBloco(BlockPlaceEvent e) {
		if (e.getBlock().getRelative(BlockFace.DOWN).getType()!=Material.AIR) {
			return;
		}
		e.getBlock().getWorld().spawnFallingBlock(e.getBlock().getLocation(), e.getBlock().getType(), e.getBlock().getData());
	}

}
