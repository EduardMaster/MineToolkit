
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class MapLoadCommand extends CommandManager {

	public MapLoadCommand() {
		super("load", "carregar");
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage("§c/e load <name>");
		} else {
			if (Mine.onlyPlayer(sender)) {
				Player p = (Player) sender;
				if (Mine.SCHEMATICS.containsKey(args[1].toLowerCase())) {
					Mine.MAPS.put(p, Mine.SCHEMATICS.get(args[1].toLowerCase()));
					p.sendMessage("§bEduardAPI §6Mapa carregado com sucesso!");
				} else {
					p.sendMessage("§bEduardAPI §cMapa invalido: §f" + args[1]);
				}

			}
		}

		return true;
	}

}
