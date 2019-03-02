
package net.eduard.api.command.api;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ApiDisableCommand extends CommandManager {

	public ApiDisableCommand() {
		super("disable", "desabilitar");
		setUsage("/api disable <plugin>");

	setDescription("Desativa um plugin ativado no servidor");
	} 

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String sub = args[1];
			if (Mine.existsPlugin(sender, sub)) {
				Plugin pl = Mine.getPlugin(sub);
				sender.sendMessage("§aPlugin desativado");
				Bukkit.getPluginManager().disablePlugin(pl);
			}

		}

		return true;
	}

}
