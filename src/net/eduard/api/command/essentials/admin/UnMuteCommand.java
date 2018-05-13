

package net.eduard.api.command.essentials.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class UnMuteCommand extends CommandManager {

	public String messageTarget = "§6O jogador §e$player §6foi desmutado por §a$sender";
	public String message = "§aVoce foi desmutado!";
	public UnMuteCommand() {
		super("unmute");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			return false;
		}

		if (Mine.existsPlayer(sender, args[0])) {
			Player target = Mine.getPlayer(args[0]);
			target.removeMetadata("muted", getPlugin());
			Mine.broadcast(
					messageTarget.replace("$player", target.getDisplayName())
							.replace("$sender", sender.getName()));
			Mine.chat(target, message);

		}
		return true;
	}
}
