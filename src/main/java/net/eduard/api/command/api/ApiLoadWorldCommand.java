package net.eduard.api.command.api;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ApiLoadWorldCommand extends CommandManager {
	public ApiLoadWorldCommand() {
		super("loadworld", "carregarmundo", "ligarmundo");
		setUsage("/api loadworld <world>");
		setDescription("Carrega um mundo descarregado no servidor");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			if (Bukkit.getWorld(args[1]) == null) {
				Mine.loadWorld(args[1]);
				sender.sendMessage("§bEduardAPI §aVoce carregou o mundo §2" + args[1]);
			} else {
				sender.sendMessage("§bEduardAPI §aEste mundo já esta carregado");
			}
		}
		return true;
	}

}
