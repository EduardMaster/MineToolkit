
package net.eduard.api.command.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class DeopCommand extends CMD {
	public DeopCommand() {
		super("deop");
	}
	public String messageTarget = "§6Voce removeu Op do jogador §e$target";
	public String message = "§6Voce agora não é mais Op!";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		if (API.existsPlayer(sender, args[0])) {
			Player target = API.getPlayer(args[0]);
			target.setOp(false);
			API.chat(target, message);
			API.chat(sender,
					messageTarget.replace("$target", target.getDisplayName()));
		}

		return true;
	}
}
