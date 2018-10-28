
package net.eduard.api.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.config.Config;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ConfigReloadCommand extends CommandManager {

	public ConfigReloadCommand() {
		super("reload", "recarregar");
		register(new ConfigReloadAllCommand());
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String cmd = args[1];
			if (Mine.existsPlugin(sender, cmd)) {
				Plugin pl = Mine.getPlugin(cmd);
				if (args.length == 2) {
					Config.reloadConfigs(pl);
					Mine.chat(sender,
							"§aRecarregando todas configura§§es do Plugin "
									+ pl.getName());
				} else {

				}
			}

		}

		return true;
	}

}
