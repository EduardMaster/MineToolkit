
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.manager.CommandManager;

public class ApiCommand extends CommandManager {

	public ApiCommand() {
		super("api");
		register(new ApiReloadCommand());
		register(new ApiUnloadWorldCommand());
		register(new ApiLoadWorldCommand());
		register(new ApiWorldsCommand());
		register(new ApiDeleteWorldCommand());
		register(new ApiListCommand());
		register(new ApiDisableCommand());
		register(new ApiEnableCommand());
		register(new ApiListCommand());
		
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("§f/"+label+" §bunloadworld §a<name>");
			sender.sendMessage("§f/"+label+" §bloadworld §a<name>");
			sender.sendMessage("§f/"+label+" §bdeleteworld §a<name>");
			sender.sendMessage("§f/"+label+" §bworlds");
			sender.sendMessage("-=--=--=--=--=--=--=--=--=--=--=--=-");
		}else {
			super.onCommand(sender, command, label, args);
		}
		
		return true;
	}
	

}
