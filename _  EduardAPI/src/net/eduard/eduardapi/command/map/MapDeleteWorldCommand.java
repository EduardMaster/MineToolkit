package net.eduard.eduardapi.command.map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.WorldAPI;

public class MapDeleteWorldCommand extends CMD {

	public MapDeleteWorldCommand() {
		super("delete","deletar");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (args.length == 1){
			return false;
		}
		
		String name = args[0];
		if (API.existsWorld(sender, name)) {
			WorldAPI.deleteWorld(name);
			sender.sendMessage(
					"§bEduardAPI §aO Mundo §2" + name + "§a foi deletado com sucesso!");
		}
		return true;
	}
}
