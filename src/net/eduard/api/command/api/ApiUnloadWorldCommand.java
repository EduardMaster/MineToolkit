package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ApiUnloadWorldCommand extends CommandManager {
	public ApiUnloadWorldCommand() {
		super("unloadworld", "descarregarmundo", "desligarmundo");
		setUsage("/api unload <world>");
		setDescription("Descarrega um mundo carregado no servidor");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			if (Mine.existsWorld(sender, args[1])) {
				Mine.unloadWorld(args[1]);
				sender.sendMessage("§bEduardAPI §aVoce descarregou o mundo §2" + args[1]);
			}
		}

		return true;
	}

}
