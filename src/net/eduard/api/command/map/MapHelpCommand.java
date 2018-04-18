package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.manager.CommandManager;

public class MapHelpCommand extends CommandManager {
	public MapHelpCommand() {
		super("help");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage("§a/map help");;
		return true;
	}
}
