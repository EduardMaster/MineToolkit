
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.manager.CommandManager;

public class ApiUnloadCommand extends CommandManager {

	public ApiUnloadCommand() {
		super("unload","descarregar");
		setUsage("/api unload <plugin>");
		setDescription("Descarrega um plugin carregado no servidor");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage("§cNão implementado");
		
		return true;
	}

}
