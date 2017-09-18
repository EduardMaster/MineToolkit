
package net.eduard.api.command.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.GameAPI;

public class CheckIpCommand extends CMD {
	public String message = "§6Seu IP é: §a$ip";
	public String messageTarget = "§6O IP do jogador $player é: §e$ip";
	
	public CheckIpCommand() {
		super("checkip");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				API.chat(p,message.replace("$ip", GameAPI.getIp(p)));
			} else
				return false;

		} else {
			if (API.existsPlayer(sender, args[0])) {
				Player target = API.getPlayer(args[0]);
				API.chat(sender,messageTarget
						.replace("$player", target.getDisplayName())
						.replace("$ip", GameAPI.getIp(target)));
			}
		}
		return true;
	}

}
