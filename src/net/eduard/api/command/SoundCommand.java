package net.eduard.api.command;

import java.util.ArrayList;
import java.util.List;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class SoundCommand extends CommandManager {

	public SoundCommand() {
		super("sound");
	}
	public static List<String> getSounds(String argument) {
		if (argument == null) {
			argument = "";
		}
		argument = argument.trim().replace("_", "");
		List<String> list = new ArrayList<>();

		for (Sound enchant : Sound.values()) {
			String text = Extra.toTitle(enchant.name(), "");
			String line = enchant.name().trim().replace("_", "");
			if (Extra.startWith(line, argument)) {
				list.add(text);
			}
		}
		return list;

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			sendUsage(sender);
		} else {
			String arg = args[0];
			Sound sound = Mine.getSound(arg);
			if (sound == null) {
				sound = Sound.LEVEL_UP;
			}
			float volume = 2;
			float pitch = 1;
			if (args.length >= 2) {
				volume = Extra.toFloat(args[1]);
			}
			if (args.length >= 3) {
				pitch = Extra.toFloat(args[2]);
			}
			Player player = null;
			if (sender instanceof Player) {
				player = (Player) sender;
			}
			if (args.length >= 4) {
				if (Mine.existsPlayer(sender, args[3])) {
					player = Mine.getPlayer(args[3]);
				} else
					return true;

			}
			if (player == null) {
				sender.sendMessage(Mine.MSG_PLAYER_NOT_EXISTS);
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

			return SoundCommand.getSounds(args[0]);
		}

		return null;
	}
}
