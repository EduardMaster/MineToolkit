
package net.eduard.api.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class WhiteListOffCommand extends CMD {

	public String message = "§aVoce desativou a WhiteList";

	public WhiteListOffCommand() {
		super("off", "disable");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Bukkit.setWhitelist(false);
		API.chat(sender,message);
		
		return true;
	}

}
