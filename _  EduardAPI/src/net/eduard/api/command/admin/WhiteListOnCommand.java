
package net.eduard.api.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class WhiteListOnCommand extends CMD {

	public String message = "§aVoce ativou a WhiteList";

	public WhiteListOnCommand() {
		super("on", "enable");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Bukkit.setWhitelist(true);
		API.chat(sender,message);
		return true;
	}

}
