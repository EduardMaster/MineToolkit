package net.eduard.api.tutorial.sistemas;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.eduard.api.manager.TimeManager;

public class CombatLog extends TimeManager{
	public static final HashMap<Player, Long> ON_COMBAT = new HashMap<>();
	@EventHandler
	public void Combat(EntityDamageByEntityEvent e) {

		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (e.getDamager() instanceof Player) {
				Player p2 = (Player) e.getDamager();
				if (!ON_COMBAT.containsKey(p)) {
					p.sendMessage("§cVoce entrou em combate!");
				}
				if (!ON_COMBAT.containsKey(p2)) {
					p.sendMessage("§cVoce entrou em combate!");
				}
				ON_COMBAT.put(p2, System.currentTimeMillis());
				ON_COMBAT.put(p, System.currentTimeMillis());
			}
		}
	}

	@EventHandler
	public void Combat(PlayerCommandPreprocessEvent e) {

		Player p = e.getPlayer();
		if (ON_COMBAT.containsKey(p)) {
			e.setCancelled(true);
			p.sendMessage("§cVoce esta em combate não pode usar comandos!");
		}
	}

	@EventHandler
	public void Combat(PlayerDeathEvent e) {

		Player p = e.getEntity();
		if (ON_COMBAT.containsKey(p)) {
			ON_COMBAT.remove(p);
			p.sendMessage("§cVoce saiu do combate!");
		}
	}

	public static final int COMBAT_LIMIT_SECONDS = 15;
	@EventHandler
	public void event(PlayerQuitEvent e) {

		Player p = e.getPlayer();
		if (ON_COMBAT.containsKey(p)) {
			Long time = ON_COMBAT.get(p);
			long now = System.currentTimeMillis();
			if (now - time > (COMBAT_LIMIT_SECONDS * 1000)) {
				ON_COMBAT.remove(p);
			} else {
				p.setHealth(0);
			}
		}
	}
}
