
package net.eduard.api.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class BanCommand extends CMD {

	public String message = "§6O jogador §e$target §6foi banido por §a$sender §6motido: §c$reason";
	public String messageTarget = "§6Voce foi banido por §e$target §6motivo: §f$reason";
	public BanCommand() {
		super("ban");
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length <= 1) {
			return false;
		}
		if (API.existsPlayer(sender, args[0])) {
			Player target = API.getPlayer(args[0]);
			StringBuilder builder = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				builder.append(args[i] + " ");
			}
			target.setBanned(true);
			target.kickPlayer(messageTarget.replace("$target", sender.getName())
					.replace("$sender", sender.getName())
					.replace("$reason", builder.toString()));
			API.broadcast(message.replace("$target", target.getDisplayName())
					.replace("$sender", sender.getName())
					.replace("$reason", builder.toString()));
		}

		return true;
	}
}
