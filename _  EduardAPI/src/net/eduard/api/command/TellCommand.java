
package net.eduard.api.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ChatAPI;
import net.eduard.api.setup.ExtraAPI;

public class TellCommand extends CMD {

	public String message = "§aPara: §f$target$> §7$message";
	public String messageTarget = "§aDe: §f$player$> §7$message";
	public String messageDisabled = "§cDe: §f$player desativou as mensagens";
	public static Map<Player, Boolean> toggle = new HashMap<>();
	public static void toggle(Player player) {
		toggle.put(player, !isToggle(player));

	}
	public static Boolean isToggle(Player player) {
		if (!toggle.containsKey(player)) {
			toggle.put(player, false);
		}
		return toggle.get(player);
	}
	public TellCommand() {
		super("tell", "privado");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length <= 1) {
			API.chat(sender, getUsage());
		} else {
			if (API.existsPlayer(sender, args[0])) {
				Player target = API.getPlayer(args[0]);
				String message = ExtraAPI.getText(1, args);
				if (!isToggle(target)) {
					API.chat(sender, messageDisabled.replace("$player",
							target.getName()));
				} else {
					API.chat(sender,
							this.message.replace("$target", target.getName())
									.replace("$>", ChatAPI.getArrowRight())
									.replace("$message", message));
					API.chat(target,
							messageTarget.replace("$player", sender.getName())
									.replace("$>", ChatAPI.getArrowRight())
									.replace("$message", message));
				}
			}
		}
		return true;
	}
}
