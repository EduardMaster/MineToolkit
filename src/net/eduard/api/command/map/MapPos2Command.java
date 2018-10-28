
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapPos2Command extends CommandManager {

	public MapPos2Command() {
		super("pos2");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			Schematic schema = Mine.getSchematic(p);
			schema.setHigh(p.getLocation().toVector());
			p.sendMessage("§bEduardAPI §6Posi§§o 2 setada!");
		}
		return true;
	}

}
