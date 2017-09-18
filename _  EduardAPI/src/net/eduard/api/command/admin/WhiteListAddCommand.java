
package net.eduard.api.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class WhiteListAddCommand extends CMD {
	public WhiteListAddCommand() {
		super("add", "adicionar");
	}
	public String message = "§bVoce adicionou o §3$player§b na Lista Branca";
	
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
		target.setWhitelisted(true);
		API.chat(sender,message.replace("$player", target.getName()));
		return true;
	}
	

}
