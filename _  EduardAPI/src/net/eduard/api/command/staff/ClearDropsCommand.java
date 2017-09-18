package net.eduard.api.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class ClearDropsCommand extends CMD {

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
			API.broadcast(message);

		} else {
			if (API.existsWorld(sender, args[0])) {
				World world = API.getWorld(args[0]);
				for (Item entity : world.getEntitiesByClass(Item.class)) {
					entity.remove();
				}
				API.broadcast(message.replace("$world", world.getName()));
			}

		}

		return true;
	}

}
