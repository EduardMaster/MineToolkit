
package net.eduard.api.command.essentials;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class WhiteListRemoveCommand extends CommandManager {
	public WhiteListRemoveCommand() {
		super("remove", "remover");
	}
	public String message = "§bVoce removeu o §3$player§b da Lista Branca";
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length <= 1) {
			sender.sendMessage("§c/" + label + " " + args[0]+" <player>");
			return true;
		}

		String name = args[1];
		OfflinePlayer target = Bukkit.getOfflinePlayer(name);
		target.setWhitelisted(false);
		Mine.chat(sender,message.replace("$player", target.getName()));
	
		return true;
	}

}
