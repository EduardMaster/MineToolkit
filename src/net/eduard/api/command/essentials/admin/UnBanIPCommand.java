
package net.eduard.api.command.essentials.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class UnBanIPCommand extends CommandManager {

	public String message = "§6O IP §e$ip §6foi desbanido!";
	public UnBanIPCommand() {
		super("unbanip");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		String ip = args[0];
		Bukkit.unbanIP(ip);
		Mine.chat(sender, message.replace("$ip", ip));

		return true;
	}
}
