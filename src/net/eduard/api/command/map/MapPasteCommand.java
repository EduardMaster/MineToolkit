
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Schematic;
import net.eduard.api.setup.manager.CommandManager;

public class MapPasteCommand extends CommandManager {

	public MapPasteCommand() {
		super("paste", "colar");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!Mine.MAPS.containsKey(p)) {
				p.sendMessage("§bEduardAPI §2Primeiro copie um Mapa:§a /e copy");
				return true;
			}

			Schematic map = Mine.MAPS.get(p);
			map.paste(p.getLocation());
			p.sendMessage("§bEduardAPI §6Mapa colado com sucesso! ($blocks)".replace("$blocks", "" + map.getCount()));
		}
		return true;
	}

}
