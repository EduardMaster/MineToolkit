
package net.eduard.api.command.essentials.admin.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class WhiteListOffCommand extends CommandManager {

	public String message = "§aVoce desativou a WhiteList";

	public WhiteListOffCommand() {
		super("off", "disable");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Bukkit.setWhitelist(false);
		Mine.chat(sender,message);
		
		return true;
	}

}
