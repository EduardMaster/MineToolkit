
package net.eduard.api.command.staff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.GameAPI;

public class ToggleOnlineCommand extends CMD {
	public String messageOn = "§6Voce esta visivel!";
	public String messageOff = "§6Voce esta invisivel!";
	public List<String> commandsOn = Arrays.asList("visible", "aparecer");
	public List<String> commandsOff = Arrays.asList("invisible", "desaparecer");
	public static List<Player> players = new ArrayList<>();
	public ToggleOnlineCommand() {
		super("toggleonline");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (commandsOn.contains(label.toLowerCase())) {
				GameAPI.show(p);
				API.chat(sender, messageOn);
				if (!players.contains(p))
					players.add(p);
			} else if (commandsOff.contains(label.toLowerCase())) {
				GameAPI.hide(p);
				API.chat(sender, messageOff);
				players.remove(p);
			} else {
				if (args.length == 0) {
					if (players.contains(p)) {
						p.chat("/" + commandsOff.get(0));

					} else {
						p.chat("/" + commandsOn.get(0));
					}
					API.all("oi");
				} else {
					String cmd = args[0].toLowerCase();
					if (API.COMMANDS_ON.contains(cmd)) {
						p.chat("/" + commandsOn.get(0));
					} else if (API.COMMANDS_OFF.contains(cmd)) {
						p.chat("/" + commandsOff.get(0));
					} else {
						API.chat(sender, getUsage());
					}
				}

			}
		}
		return true;
	}

}
