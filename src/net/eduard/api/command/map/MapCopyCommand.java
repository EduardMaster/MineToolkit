
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class MapCopyCommand extends CommandManager {

	public MapCopyCommand() {
		super("copy","copiar");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!Mine.POSITION1.containsKey(p)) {
				p.sendMessage("§bEduardMine §2Posição 1 não foi setada!");
				return true;
			}
			if (!Mine.POSITION2.containsKey(p)) {
				p.sendMessage("§bEduardMine §2Posição 2 não foi setada!");
				return true;
			}
			
//			Mine.MAPS.put(p, new Arena(Mine.POSITION1.get(p),
//					Mine.POSITION2.get(p), p.getLocation()));
			p.sendMessage("§bEduardMine §6Mapa copiado!");
		}
		return true;
	}

}
