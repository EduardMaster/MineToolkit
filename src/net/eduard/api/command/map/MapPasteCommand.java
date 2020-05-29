
package net.eduard.api.command.map;

import net.eduard.api.EduardAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapPasteCommand extends CommandManager {

	public MapPasteCommand() {
		super("paste", "colar");
		setDescription("Cola o Schematic (Mapa) no local que estiver");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!EduardAPI.Companion.getMAPS_CACHE().containsKey(p)) {
				p.sendMessage("§bEduardAPI §aPrimeiro copie um Mapa:§2 /map copy");
				return true;
			}

			Schematic map = EduardAPI.Companion.getMAPS_CACHE().get(p);
			map.paste(p.getLocation());
			p.sendMessage("§bEduardAPI §aMapa colado com sucesso! §2($blocks)".replace("$blocks", "" + map.getCount()));
		}
		return true;
	}

}
