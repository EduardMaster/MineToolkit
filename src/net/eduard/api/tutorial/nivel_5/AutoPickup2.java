package net.eduard.api.tutorial.nivel_5;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.eduard.api.setup.Mine;

public class AutoPickup2 implements Listener {
	@EventHandler
	public void event(BlockBreakEvent e) {
		Mine.TIME.delay(1L,new Runnable() {
			
			@Override
			public void run() {
				for (Entity block : e.getPlayer().getNearbyEntities(2, 2, 2)) {
					if (block instanceof Item) {
						Item item = (Item) block;
						e.getPlayer().getInventory().addItem(item.getItemStack());
						item.remove();
					}
				}
			}
		});

	}
}
