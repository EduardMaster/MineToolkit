
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.core.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class MapSaveCommand extends CommandManager {

	public MapSaveCommand() {
		super("save");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage("§c/map save <name>");
		} else {

			if (Mine.onlyPlayer(sender)) {
				Player p = (Player) sender;
				if (!Mine.MAPS.containsKey(p)) {
					p.sendMessage("§bEduardAPI §2Primeiro copie um Mapa:§a /e copy");
					return true;
				}
				Mine.SCHEMATICS.put(args[1].toLowerCase(), Mine.MAPS.get(p));
				p.sendMessage("§bEduardAPI §6Mapa salvado com sucesso!");
			}
		}
		return true;
	}

}
