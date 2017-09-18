
package net.eduard.eduardapi.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.server.Arena;

public class MapCopyCommand extends CMD {

	public MapCopyCommand() {
		super("copy","copiar");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!API.POSITION1.containsKey(p)) {
				p.sendMessage("§bEduardAPI §2Posição 1 não foi setada!");
				return true;
			}
			if (!API.POSITION2.containsKey(p)) {
				p.sendMessage("§bEduardAPI §2Posição 2 não foi setada!");
				return true;
			}
			
			API.MAPS.put(p, new Arena(API.POSITION1.get(p),
					API.POSITION2.get(p), p.getLocation()).copy());
			p.sendMessage("§bEduardAPI §6Mapa copiado!");
		}
		return true;
	}

}
