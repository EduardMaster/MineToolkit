
package net.eduard.api.command.api;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.manager.CommandManager;

public class ApiListCommand extends CommandManager {

	public ApiListCommand() {
		super("list", "plugins");
		setDescription("Mostra todos os plugins do servidor");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("§bEduardAPI §aLista de Plugins no servidor");
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			String color = "§c§l";
			if (plugin.isEnabled()) {
				color = "§a§l";

			}
			
			sender.sendMessage(color + plugin.getName() + " §8- §7" + plugin.getDescription().getDescription());
		}

		return true;
	}

}
