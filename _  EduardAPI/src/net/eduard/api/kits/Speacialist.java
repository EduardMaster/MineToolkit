package net.eduard.api.kits;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.game.Ability;
import net.eduard.api.game.PlayerClick;
import net.eduard.api.game.PlayerClickEffect;
import net.eduard.api.setup.ItemAPI;

public class Speacialist extends Ability {

	public int xpAmount = 1;

	public Speacialist() {
		setIcon(Material.BOOK, "§fGanhe um Kit Aleatorio",
				"§fA cada morte ganhe Xp para encantar seus itens");
		add(Material.BOOK);
		setClick(new PlayerClick(Material.BOOK,new PlayerClickEffect() {
			
			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					player.openEnchanting(player.getLocation(), true);
				}
			}
		}));

	}

	@EventHandler
	public void event(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (e.getEntity().getKiller() != null) {
			Player killer = e.getEntity().getKiller();
			if (hasKit(p)) {
				ItemAPI.drop(killer, new ItemStack(Material.EXP_BOTTLE, xpAmount));
			}
		}
	}
}
