package net.eduard.api.kits;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.util.Vector;

import net.eduard.api.API;
import net.eduard.api.game.Ability;
import net.eduard.api.minecraft.v1_7_2_R4.GrapplerHook;

public class Grappler extends Ability {
	public static HashMap<Player, GrapplerHook> hooks = new HashMap<>();
	@EventHandler
	public void event(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getMaterial() == Material.LEASH) {
			if (hasKit(player)) {
				if (!onCooldown(player)) {
					if (event.getAction() == Action.LEFT_CLICK_AIR) {
						if (hooks.containsKey(player)) {
							hooks.get(player).remove();
							hooks.remove(player);
						}
						hooks.put(player, new GrapplerHook(player, 1.5));
					} else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
						if (hooks.containsKey(player)) {
							GrapplerHook hook = hooks.get(player);
							if (hook.isHooked) {
								player.setFallDistance(-10);
								Location target = hook.getBukkitEntity()
										.getLocation();
								Vector velocity = GrapplerHook.moveTo(
										player.getLocation(), target, 0.5, 1.5,
										0.5, 0.04, 0.06, 0.04);
								player.setVelocity(velocity);
							} else {
								player.sendMessage(
										"§6O gancho nao se prendeu em nada!");
							}
						}
					}
				} else {
					player.sendMessage(
							"§6Voce esta em PvP e não pode usar o Kit!");
				}
			}
		}
	}
	public Grappler() {
		setIcon(Material.LEASH, "§fSe mova mais rapido");
		add(Material.LEASH);
		setActiveCooldownOnPvP(true);
		setTime(5);
		setPrice(50 * 1000);
	}

	@EventHandler
	public void event(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		if (hooks.containsKey(p)) {
			hooks.get(p).remove();
			hooks.remove(p);
		}
	}

	@EventHandler
	public void event(PluginDisableEvent e) {
		if (getPlugin().equals(e.getPlugin()))
			for (Player p : API.getPlayers()) {
				if (hooks.containsKey(p)) {
					hooks.get(p).remove();
					hooks.remove(p);
				}
			}

	}

}
