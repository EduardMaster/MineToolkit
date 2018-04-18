package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.manager.CommandManager;

public class MapWorldCommand extends CommandManager {

	public MapWorldCommand() {
		super("world","mundo");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		return true;
	}
}
