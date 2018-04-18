
package net.eduard.api.command.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class StrikeLightningCommand extends CommandManager {
	public String message = "";
	public StrikeLightningCommand() {
		super("strike");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		if (Mine.existsPlayer(sender, args[0])) {
			Player target = Mine.getPlayer(args[0]);
			target.getWorld().strikeLightning(target.getLocation());
		}
		
		return true;
	}
}
