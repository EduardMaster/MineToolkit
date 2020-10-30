package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.manager.CommandManager;

public class MapHelpCommand extends CommandManager {
	public MapHelpCommand() {
		super("help","ajuda","?");
		setDescription("Mostra os comandos existentes");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (CommandManager sub : getParent().getSubCommands().values()) {
			if (sender.hasPermission(sub.getPermission())) {
				sender.sendMessage("§a"+ sub.getUsage() + " §8-§7 " + sub.getDescription());
			}
		}
		return true;
	}
}
