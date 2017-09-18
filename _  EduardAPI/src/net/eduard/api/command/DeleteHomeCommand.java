package net.eduard.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;

public class DeleteHomeCommand extends CMD {
	public DeleteHomeCommand() {
		super("deletehome");
	}
	public Config config = new Config("homes.yml");
	public String message = "§aVoce deletou sua home §2$home!";
	public String messageError = "§cNão existe a home $home!";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			}else {
				String path = p.getUniqueId().toString()+"."+args[0].toLowerCase();
				if (config.contains(path)) {
					config.remove(path);
					API.chat(sender, message.replace("$home", ""));
					
				} else {
					API.chat(sender, messageError.replace("$home", ""));
				}
			}
		}

		return true;
	}

}
