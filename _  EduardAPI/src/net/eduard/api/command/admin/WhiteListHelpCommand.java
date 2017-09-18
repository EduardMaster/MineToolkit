
package net.eduard.api.command.admin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class WhiteListHelpCommand extends CMD {

	private List<String> messages = new ArrayList<>();

	public WhiteListHelpCommand() {
		super("help", "ajuda");
		messages.add("§6      Tutorial      ");
		messages.add("§a/whitelist help|list");
		messages.add("§a/whitelist on|off");
		messages.add("§a/whitelist add|remove <player>");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for (String line : messages) {
			API.chat(sender,line);
		}
		
		return true;
	}

}
