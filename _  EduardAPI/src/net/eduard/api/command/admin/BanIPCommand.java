
package net.eduard.api.command.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class BanIPCommand extends CMD {

	public String message = "§6O IP do jogador §e$player §6foi banido por §a$sender §6motido: §c$reason";
	public String messageTarget = "§6Seu IP foi banido por §e$sender §6motivo: §f$reason";
	public BanIPCommand() {
		super("banip");
	}
	@Override
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
			String ip = target.getAddress().getAddress().getHostAddress();
			Bukkit.getServer().banIP(ip);
			target.kickPlayer(messageTarget.replace("$sender", sender.getName())
					.replace("$sender", sender.getName())
					.replace("$reason", builder.toString()));
			API.broadcast(message.replace("$player", target.getDisplayName())
					.replace("$player", sender.getName())
					.replace("$reason", builder.toString()));
		}

		return true;
	}
}
