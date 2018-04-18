package net.eduard.api.tutorial.nivel_4;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.setup.Mine;

public class RegenerarArenas implements Listener{
	public static final ItemStack REGENERAR =
			Mine.newItem(Material.APPLE,"§6Regenerar");

		public static final ItemStack RESETAR =
				Mine.newItem(Material.ARROW,"§6Resetar");
		@EventHandler
		public void ResetarBlocos(BlockBurnEvent e) {

			if (!BLOCKS.contains(e.getBlock().getState())) {
				BLOCKS.add(e.getBlock().getState());
			}
		}

		@EventHandler
		public void ResetarBlocos(EntityExplodeEvent e) {

			for (Block block : e.blockList()) {
				if (!BLOCKS.contains(block.getState())) {
					BLOCKS.add(block.getState());
				}
			}
		}

		@EventHandler
		public void ResetarBlocos(PlayerJoinEvent e) {

			Player p = e.getPlayer();
			p.getInventory().clear();
			p.getInventory().addItem();
			p.getInventory().addItem(REGENERAR);
			p.getInventory().addItem(RESETAR);
		}

		@EventHandler
		public void ResetarBlocos(PlayerInteractEvent e) {

			if (e.getItem() == null) {
				return;
			}
			if (e.getAction() == Action.LEFT_CLICK_AIR
				| e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (e.getItem().equals(REGENERAR)) {
					e.setCancelled(true);
					regenerar();
				} else if (e.getItem().equals(RESETAR)) {
					e.setCancelled(true);
					resetar();
				}

			}
		}
		public static final ArrayList<BlockState> BLOCKS = new ArrayList<>();
		@SuppressWarnings("deprecation")
		public void regenerar() {

			for (BlockState state : BLOCKS) {
				Block block = state.getBlock();
				block.setTypeIdAndData(state.getTypeId(), state.getData().getData(),
					false);
			}
			Bukkit.broadcastMessage("§6Regenerado!");
		}

		public void resetar() {
			BLOCKS.clear();
			Bukkit.broadcastMessage("§6Resetado!");
		}
}
