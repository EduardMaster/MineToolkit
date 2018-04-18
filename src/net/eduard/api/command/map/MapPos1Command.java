
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class MapPos1Command extends CommandManager {

	public MapPos1Command() {
		super("pos1");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			Mine.POSITION1.put(p, p.getLocation());
			p.sendMessage("§bEduardMine §6Posição 1 setada!");
		}
		return true;
	}

}
