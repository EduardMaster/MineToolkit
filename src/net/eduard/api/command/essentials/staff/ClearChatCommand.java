
package net.eduard.api.command.essentials.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class ClearChatCommand extends CommandManager {
	public String message = "§6O $sender limpou o Chat!";
	public int size = 50;
	public ClearChatCommand() {
		super("clearchat");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for (int i = 0; i < size; i++) {
			Mine.broadcast(" ");
		}
		Mine.broadcast(message.replace("$sender", sender.getName()));
		return true;
	}

}
