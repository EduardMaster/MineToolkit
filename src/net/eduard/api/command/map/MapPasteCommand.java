
package net.eduard.api.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class MapPasteCommand extends CommandManager {

	public MapPasteCommand() {
		super("paste", "colar");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
//			Player p = (Player) sender;
//			if (!Mine.MAPS.containsKey(p)) {
//				p.sendMessage(
//						"§bEduardMine §2Primeiro copie um Mapa:§a /e copy");
//				return true;
//			}

//			Arena map = Mine.MAPS.get(p);
//			map.copyPaste(p.getLocation());
//			p.sendMessage("§bEduardMine §6Mapa colado com sucesso! ($blocks)"
//					.replace("$blocks", "" + map.getBlocks().size()));
		}
		return true;
	}

}
