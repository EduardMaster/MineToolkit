
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.core.Mine;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapPasteCommand extends CommandManager {

	public MapPasteCommand() {
		super("paste", "colar");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!Mine.MAPS_CONFIGURING.containsKey(p)) {
				p.sendMessage("§bEduardAPI §2Primeiro copie um Mapa:§a /map copy");
				return true;
			}

			Schematic map = Mine.MAPS_CONFIGURING.get(p);
			map.paste(p.getLocation());
			p.sendMessage("§bEduardAPI §6Mapa colado com sucesso! ($blocks)".replace("$blocks", "" + map.getCount()));
		}
		return true;
	}

}
