package net.eduard.api.command.essentials.staff;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class ClearDropsCommand extends CommandManager {

	public String message = "§6Os drops foram removidos!";

	public String messageTarget = "§6Os drops foram removidos no mundo $world!";

	public ClearDropsCommand() {
		super("cleardrops");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 0) {
			for (World world : Bukkit.getWorlds()) {
				for (Item entity : world.getEntitiesByClass(Item.class)) {
					entity.remove();
				}
			}
			Mine.broadcast(message);

		} else {
			if (Mine.existsWorld(sender, args[0])) {
				World world = Mine.getWorld(args[0]);
				for (Item entity : world.getEntitiesByClass(Item.class)) {
					entity.remove();
				}
				Mine.broadcast(message.replace("$world", world.getName()));
			}

		}

		return true;
	}

}
