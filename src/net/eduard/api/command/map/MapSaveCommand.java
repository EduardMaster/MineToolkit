
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class MapSaveCommand extends CommandManager {

	public MapSaveCommand() {
		super("save","salvar");
		setUsage("/map save <name>");
		setDescription("Salva o Schematic (Mapa) copiado");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {

			if (Mine.onlyPlayer(sender)) {
				Player p = (Player) sender;
				if (!Mine.MAPS_CACHE.containsKey(p)) {
					p.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy");
					return true;
				}
				Mine.MAPS.put(args[1].toLowerCase(), Mine.MAPS_CACHE.get(p));
				p.sendMessage("§bEduardAPI §aMapa salvado com sucesso!");
			}
		}
		return true;
	}

}
