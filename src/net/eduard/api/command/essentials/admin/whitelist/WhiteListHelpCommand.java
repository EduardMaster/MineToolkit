
package net.eduard.api.command.essentials.admin.whitelist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class WhiteListHelpCommand extends CommandManager {

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
			Mine.chat(sender,line);
		}
		
		return true;
	}

}
