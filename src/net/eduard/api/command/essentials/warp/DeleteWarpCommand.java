package net.eduard.api.command.essentials.warp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Sounds;
import net.eduard.api.setup.manager.CommandManager;

public class DeleteWarpCommand extends CommandManager {
	public DeleteWarpCommand() {
		super("deletewarp");

	}
	public Config config = new Config("warps.yml");
	public Sounds sound = Sounds.create("ENDERMAN_TELEPORT");
	public String message = "§6Voce deletou o warp $warp";
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
				if (config.contains(path)) {

					Mine.chat(p, message.replace("$warp", warp));
				} else {
					Mine.chat(p, messageError.replace("$warp", warp));
				}
				config.remove(path);
			}

		}

		return true;
	}

}
