
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.manager.CommandManager;

public class ApiHelpCommand extends CommandManager {

	public ApiHelpCommand() {
		super("help","ajuda","?");
		setDescription("Mostra uma lista de comandos");
	
		
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (CommandManager sub : getCommand("api").getCommands().values()) {
			if (sender.hasPermission(sub.getPermission())) {
				sender.sendMessage("§a"+ sub.getUsage() + " §8-§7 " + sub.getDescription());
			}
		}
		
		return true;
	}
	

}
