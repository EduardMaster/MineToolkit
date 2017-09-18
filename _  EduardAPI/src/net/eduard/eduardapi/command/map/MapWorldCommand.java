package net.eduard.eduardapi.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.manager.CMD;

public class MapWorldCommand extends CMD {

	public MapWorldCommand() {
		super("world","mundo");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		return true;
	}
}
