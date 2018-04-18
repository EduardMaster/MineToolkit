package net.eduard.api.tutorial.nivel_3;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.eduard.api.config.Config;

public class ChatComDelay implements Listener {

	public static final Config cooldown = new Config("cooldown.yml");
	public static int cooldownSeconds = 10;

	@EventHandler
	private void chat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (onCooldown(p)) {
			e.setCancelled(true);
		}
	}

	private boolean onCooldown(Player p) {
		if (cooldown.getConfig().contains(p.getUniqueId().toString())) {
			long before = cooldown.getConfig().getLong(p.getUniqueId().toString());
			long now = System.currentTimeMillis();
			int time = cooldownSeconds * 1000;
			// 10     7     15 =  -2
			long div = (-now+(before+time))/1000;
			if (now < (before + time)) {
				p.sendMessage("§cNão pode conversar espere mais "+div+" segundos!");
				return true;
			} else {
				cooldown.getConfig().set(p.getUniqueId().toString(), null);
				return onCooldown(p);
			}
		} else {
			p.sendMessage("§fVoce não vai poder falar durante "+cooldownSeconds+" segundos!");
			setOnCooldown(p);
			return false;

		}

	}
	private void setOnCooldown(Player p) {
		cooldown.getConfig().set(p.getUniqueId().toString(), System.currentTimeMillis());
		cooldown.saveConfig();
	}

}
