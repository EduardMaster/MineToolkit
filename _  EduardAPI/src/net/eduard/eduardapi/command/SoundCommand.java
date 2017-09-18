package net.eduard.eduardapi.command;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ExtraAPI;

public class SoundCommand extends CMD {

	public SoundCommand() {
		super("sound");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			sendUsage(sender);
		} else {
			String arg = args[0];
			Sound sound = ExtraAPI.getSound(arg);
			if (sound == null) {
				sound = Sound.LEVEL_UP;
			}
			float volume = 2;
			float pitch = 1;
			if (args.length >= 2) {
				volume = ExtraAPI.toFloat(args[1]);
			}
			if (args.length >= 3) {
				pitch = ExtraAPI.toFloat(args[2]);
			}
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
			if (args.length >= 4) {
				if (API.existsPlayer(sender, args[3])) {
					player = API.getPlayer(args[3]);
				} else
					return true;

			}
			if (player == null) {
				sender.sendMessage(API.PLAYER_NOT_EXISTS);
				return true;
			}

			boolean world = false;
			Location location = player.getLocation();
			if (world) {
				player.getWorld().playSound(location, sound, volume, pitch);
			} else
				player.playSound(location, sound, volume, pitch);
			sender.sendMessage("§aEfeito sonoro criado!");
		}

		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {

			return ExtraAPI.getSounds(args[0]);
		}

		return null;
	}
}
