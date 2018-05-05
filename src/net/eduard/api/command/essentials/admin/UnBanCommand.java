
package net.eduard.api.command.essentials.admin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class UnBanCommand extends CommandManager {

	public String message = "§6O jogador §e$target §6foi desabanido!";
	
	public UnBanCommand() {
		super("unban");
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		String name = args[0];
		OfflinePlayer target = Bukkit.getOfflinePlayer(name);
		target.setBanned(false);
		Mine.chat(sender,message.replace("$target", target.getName()));
		

		return true;
	}
}
