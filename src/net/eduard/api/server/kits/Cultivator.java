package net.eduard.api.server.kits;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.eduard.api.server.kit.KitAbility;

public class Cultivator extends KitAbility {

	public Cultivator() {
		setIcon(Material.COAL, "�fSegue seus inimigos");
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (hasKit(p)) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (e.getMaterial() == Material.AIR) {
					Block b = e.getClickedBlock();
					if (b.getType() == Material.CROPS) {
						b.setData((byte) 7, true);
					}
				}
			}
		}

	}
	public TreeType getTree(int data) {
		TreeType type = TreeType.TREE;
		switch (data) {
			case 0 :
				type = TreeType.TREE;
				break;
			case 1 :
				type = TreeType.REDWOOD;
				break;
			case 2 :
				type = TreeType.BIRCH;
				break;
			case 3 :
				type = TreeType.JUNGLE;
				break;
			default :
				type = TreeType.TREE;
		}
		return type;
	}
}
