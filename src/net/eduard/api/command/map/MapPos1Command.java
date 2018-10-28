
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapPos1Command extends CommandManager {

	public MapPos1Command() {
		super("pos1");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			Schematic schema = Mine.getSchematic(p);
			schema.setLow(p.getLocation().toVector());
			p.sendMessage("§bEduardAPI §6Posi§§o 1 setada!");
		}
		return true;
	}

}
