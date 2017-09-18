package net.eduard.api.command;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;

public class HomesCommand extends CMD {
	public HomesCommand() {
		super("homes");

	}
	public Config config = new Config("homes.yml");
	public String message = "§6Suas homes: §e$homes";
	public String messageError = "§cVocê não possui nenhuma home!";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;

			Set<String> list = config.getKeys(p.getUniqueId().toString());
			if (list.size() == 0) {
				API.chat(p, messageError);
			} else {
				StringBuilder builder = new StringBuilder();
				int id = 0;
				for (String sec : list) {
					if (id != 0) {
						builder.append(", ");
					}
					id++;
					builder.append(sec);
				}
				API.chat(p, message.replace("$homes", builder.toString()));
			}

		}

		return true;
	}
}
