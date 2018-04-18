package net.eduard.api.command.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class SetWarpCommand extends CommandManager {
	public SetWarpCommand() {
		super("setwarp");

	}
	public Config config = new Config("warps.yml");
	public String message = "§bO Warp §3$warp §bfoi setado!";
	public String messageError = "§cNão existe este Warp";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			} else {
				String warp = args[0];
				String path = warp.toLowerCase();
				config.set(path, p.getLocation());
				Mine.chat(p, message.replace("$warp", warp));
			}
		}
		return true;
	}

}
