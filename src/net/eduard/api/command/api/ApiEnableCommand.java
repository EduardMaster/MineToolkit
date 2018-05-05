
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.manager.CommandManager;

public class ApiEnableCommand extends CommandManager {

	public ApiEnableCommand() {
		super("enable","habilitar");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length ==0) {
			
	
		}
		
		return true;
	}

}
