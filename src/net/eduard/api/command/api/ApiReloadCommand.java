
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.server.EduardPlugin;

public class ApiReloadCommand extends CommandManager {

	public ApiReloadCommand() {
		super("reload", "recarregar");
		setUsage("/api reload <plugin>");
		setDescription("Recarrega um plugin feito pelo Eduard");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender );
		} else {
			String pluginName = args[1];
			if (Mine.existsPlugin(sender, pluginName)) {
				Plugin plugin = Mine.getPlugin(pluginName);
				if (plugin instanceof EduardPlugin) {
					EduardPlugin eduardPlugin = (EduardPlugin) plugin;
					eduardPlugin.reload();
					sender.sendMessage("§bEduardAPI §aPlugin do Eduard §2"+plugin.getName()+"§a foi recarregado");
					
				}else {
					sender.sendMessage("§cEste plugin nao é um plugin do Eduard");
				}
			}
		}

		return true;
	}

}
