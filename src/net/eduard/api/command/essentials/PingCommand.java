
package net.eduard.api.command.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class PingCommand extends CommandManager {

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
				Mine.chat(p,message.replace("$ping", Mine.getPing(p)));
			} else
				return false;
		} else {
			String name = args[0];
			if (Mine.existsPlayer(sender, name)) {
				Player target = Mine.getPlayer(name);
				Mine.chat(sender,messageTarget
						.replace("$target", target.getDisplayName())
						.replace("$ping", Mine.getPing(target)));
			}
		}
		return true;
	}

}
