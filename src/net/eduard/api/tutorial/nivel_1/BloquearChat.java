package net.eduard.api.tutorial.nivel_1;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
/**
 * Sistema de player mutado
 * @author Eduard
 *
 */
public class BloquearChat implements Listener{
	public final static ArrayList<Player> MUTEDS = new ArrayList<>();
	@EventHandler
	public void Mutado(AsyncPlayerChatEvent e) {

		Player p = e.getPlayer();
		if (MUTEDS.contains(p)) {
			e.setCancelled(true);
			p.sendMessage("§cVoce foi silenciado!");
		}
	}
	
}
