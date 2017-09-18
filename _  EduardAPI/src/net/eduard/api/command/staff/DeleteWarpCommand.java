package net.eduard.api.command.staff;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.game.Sounds;
import net.eduard.api.manager.CMD;

public class DeleteWarpCommand extends CMD {
	public DeleteWarpCommand() {
		super("deletewarp");

	}
	public Config config = new Config("warps.yml");
	public Sounds sound = Sounds.create(Sound.ENDERMAN_TELEPORT);
	public String message = "§6Voce deletou o warp $warp";
	public String messageError = "§cNão existe este Warp";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {

			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			} else {
				String warp = args[0];
				String path = warp.toLowerCase();
				if (config.contains(path)) {

					API.chat(p, message.replace("$warp", warp));
				} else {
					API.chat(p, messageError.replace("$warp", warp));
				}
				config.remove(path);
			}

		}

		return true;
	}

}
