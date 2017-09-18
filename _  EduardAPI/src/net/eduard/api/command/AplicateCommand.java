
package net.eduard.api.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class AplicateCommand extends CMD {

	public List<String> messages = new ArrayList<>();
	public AplicateCommand() {
		super("aplicate" ); 
		messages.add(API .SERVER_TAG+"§a - Requisitos para ser STAFF - ");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for (String line : messages) {
			sender.sendMessage(line);
		}
		return true;
	}

}
