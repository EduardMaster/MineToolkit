package net.eduard.api.command.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class DeleteHomeCommand extends CommandManager {
	public DeleteHomeCommand() {
		super("deletehome");
	}
	public Config config = new Config("homes.yml");
	public String message = "§aVoce deletou sua home §2$home!";
	public String messageError = "§cNão existe a home $home!";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			}else {
				String path = p.getUniqueId().toString()+"."+args[0].toLowerCase();
				if (config.contains(path)) {
					config.remove(path);
					Mine.chat(sender, message.replace("$home", ""));
					
				} else {
					Mine.chat(sender, messageError.replace("$home", ""));
				}
			}
		}

		return true;
	}

}
