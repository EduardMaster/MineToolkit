package net.eduard.api.tutorial.eventos;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AlterarChat implements Listener{

	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		String format = "<tag> <player> > <message>";
		format = format.replace("<message>", e.getMessage());
		format = format.replace("<player>", p.getDisplayName());
		if (p.hasPermission("tag.adm")) {
			format = format.replace("<tag>", "§cADM");
		}else if (p.hasPermission("tag.mod")) {
			format = format.replace("<tag>", "§cMOD");
		}
		e.setFormat(format);
	}
}
