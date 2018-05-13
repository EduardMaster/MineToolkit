
package net.eduard.api.command.essentials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.storage.manager.CommandManager;

public class AplicateCommand extends CommandManager {

	public List<String> messages = new ArrayList<>();
	public AplicateCommand() {
		super("aplicate" ); 
		messages.add("§a - Requisitos para ser STAFF - ");

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
