
package net.eduard.api.command.api;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ApiEnableCommand extends CommandManager {

	public ApiEnableCommand() {
		super("enable","habilitar");
		setUsage("/api enable <plugin>");
		setDescription("Ativa um plugin desativado no servidor");
	 
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		}else {
			String pluginName = args[1];
			if (Mine.existsPlugin(sender, pluginName)) {
				Plugin plugin = Mine.getPlugin(pluginName);
				sender.sendMessage("§bEduardAPI §aPlugin §2"+pluginName+ "§a foi ativado");
				Bukkit.getPluginManager().enablePlugin(plugin);
			}
		
		}
		
		
		return true;
	}

}
