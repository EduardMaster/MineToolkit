
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.manager.CommandManager;

public class ApiLoadCommand extends CommandManager {

	public ApiLoadCommand() {
		super("load","carregar");
		setUsage("/api load <plugin>");
		setDescription("Carrega um plugin descarregado no servidor");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage("§bEduardAPI §cNão implementado");
		
		return true;
	}

}
