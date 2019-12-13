package net.eduard.api.server.kits;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.click.ClickComparationType;
import net.eduard.api.lib.click.PlayerClick;
import net.eduard.api.lib.click.PlayerClickEffect;
import net.eduard.api.server.kit.KitAbility;

public class EnderMage extends KitAbility {

	public double range = 2;
	public double high = 100;
	public int effectSeconds = 5;

	public EnderMage() {
		setIcon(Material.ENDER_PORTAL_FRAME, "�fPuxe os seus inimigos");
		add(Material.ENDER_PORTAL_FRAME);
		setTime(5);
		setMessage("�6Voce esta invuneravel por 5 segundos");
		
		PlayerClick playerClick = new PlayerClick(Material.ACACIA_DOOR, new PlayerClickEffect() {

			@Override
			public void onClick(Player player, Block block, ItemStack item) {
				// TODO Auto-generated method stub
				if (hasKit(player)) {
					if (noAir(block)) {
						return;
					}
					BlockState state = block.getState();
					state.getBlock().setType(Material.ENDER_PORTAL_FRAME);
					// e.setCanceled(false);
					player.setItemInHand(null);
					Mine.TIME.asyncTimer(new BukkitRunnable() {
						int x = effectSeconds;
						@Override
						public void run() {
							x--;
							if (check(player,
									block.getLocation().add(0, 1, 0))) {
								x = 0;
							}
							if (x == 0) {
								player.getInventory()
										.addItem(getItems().get(0));
								state.update(true, true);
								cancel();
							}
						}
					},20, 20);

				}
			}
		});
		setClick(playerClick);
		playerClick.setComparationType(ClickComparationType.ON_BLOCK);
	}

	public boolean check(Player p, Location teleport) {
		boolean pulled = false;
		List<Entity> list = p.getNearbyEntities(range, high, range);
		list.remove(p);
		for (Entity entity : list) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;
				if (livingEntity instanceof Player) {
					Player player = (Player) entity;
				player.sendMessage(getMessage());
					Mine.makeInvunerable(player, effectSeconds);
					Mine.makeInvunerable(p, effectSeconds);
					pulled = true;
					Mine.teleport(entity, teleport);
				}
			}

		}

		if (pulled) {
			p.sendMessage(getMessage());
			Mine.makeInvunerable(p, effectSeconds);
			Mine.teleport(p, teleport);
		}

		return pulled;
	}

	public boolean noAir(Block block) {
		boolean noAir = false;
		for (int x = 0; x > 2; x++) {
			block = block.getRelative(BlockFace.UP);
			noAir = block.getType() != Material.AIR;
		}
		return noAir;
	}
}
