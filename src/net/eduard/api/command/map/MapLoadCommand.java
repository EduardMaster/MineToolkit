
package net.eduard.api.command.map;

import net.eduard.api.EduardAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class MapLoadCommand extends CommandManager {

	public MapLoadCommand() {
		super("load", "carregar");
		setDescription("Carrega um mundo descarregado");
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage("§c/map load <name>");
		} else {
			if (Mine.onlyPlayer(sender)) {
				Player p = (Player) sender;
				if (EduardAPI.Companion.getMAPS().containsKey(args[1].toLowerCase())) {
					EduardAPI.Companion.getMAPS_CACHE().put(p, EduardAPI.Companion.getMAPS().get(args[1].toLowerCase()));
					p.sendMessage("§bEduardAPI §aMapa carregado com sucesso!");
				} else {
					p.sendMessage("§bEduardAPI §aMapa invalido: §2" + args[1]);
				}

			}
		}

		return true;
	}

}
