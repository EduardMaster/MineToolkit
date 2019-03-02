package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ApiDeleteWorldCommand extends CommandManager {

	public ApiDeleteWorldCommand() {
		super("deleteworld", "deletarmundo");
		setUsage("/api deleteworld <world>");
		setDescription("Deleta um mundo do servidor");

	} 

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String name = args[1];
			if (Mine.existsWorld(sender, name)) {
				Mine.deleteWorld(name);
				sender.sendMessage("§bEduardAPI §aO Mundo §2" + name + "§a foi deletado com sucesso!");
			}
		}
		return true;
	}
}
