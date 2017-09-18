package net.eduard.api.command;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ExtraAPI;

public class SetHomeCommand extends CMD {
	public SetHomeCommand() {
		super("sethome");
	}
	public Config config = new Config("homes.yml");
	public String message = "§bVoce setou um Home $home";
	public int maxHomes = 100;
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {

			Player p = (Player) sender;
			String home = "home";
			if (args.length >= 1) {
				home = args[0];
			}
			// home.limit.5
			String path = p.getUniqueId().toString() + "." + home;
			Set<String> size = config.getKeys(p.getUniqueId().toString());
			int amount = size.size();
			if (!config.contains(path)) {
				if (!ExtraAPI.hasPerm(p, getPermission(), 100, amount + 1)) {

					p.sendMessage(
							"§cVoce não tem permissão para setar mais Homes!");
					config.remove(path);
					return true;
				}
			}
			config.set(path, p.getLocation());
			API.chat(p, message.replace("$home", home));

		}

		return true;
	}

}
