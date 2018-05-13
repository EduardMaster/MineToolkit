
package net.eduard.api.command.essentials.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class ToggleChatCommand extends CommandManager {

	public boolean chatEnabled = true;
	public String messageOn = "§6Chat ativado!";
	public String messageOff = "§6Chat foi temporariamente desativado!";
	public String messageDisabled = "§cChat desativado!";
	public String chatPerm = "togglechat.bypass";
	
	 
	public ToggleChatCommand() {
		super("chat");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (chatEnabled) {
			chatEnabled = false;
			Mine.broadcast(messageOff);
		} else {
			chatEnabled = true;
			Mine.broadcast(messageOn);
		}
		return true;
	}
	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (!chatEnabled && !p.hasPermission(chatPerm)) {
			e.setCancelled(true);
			Mine.chat(p,messageDisabled);
		}
	}

}
