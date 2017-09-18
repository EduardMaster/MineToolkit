
package net.eduard.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.RexAPI;

public class PingCommand extends CMD {

	public String message = "§6Seu ping é: §e$ping";
	public String messageTarget = "§6O ping do jogador §e$target §6é: §a$ping";
	public PingCommand() {
		super("ping");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				API.chat(p,message.replace("$ping", RexAPI.getPing(p)));
			} else
				return false;
		} else {
			String name = args[0];
			if (API.existsPlayer(sender, name)) {
				Player target = API.getPlayer(name);
				API.chat(sender,messageTarget
						.replace("$target", target.getDisplayName())
						.replace("$ping", RexAPI.getPing(target)));
			}
		}
		return true;
	}

}
