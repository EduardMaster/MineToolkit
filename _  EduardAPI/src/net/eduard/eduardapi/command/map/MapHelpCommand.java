package net.eduard.eduardapi.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.manager.CMD;

public class MapHelpCommand extends CMD {
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
