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
			String worldName = args[1];
			if (Mine.existsWorld(sender, worldName)) {
				Mine.deleteWorld(worldName);
				sender.sendMessage("§bEduardAPI §aO Mundo §2" + worldName + "§a foi deletado com sucesso!");
			}
		}
		return true;
	}
}
