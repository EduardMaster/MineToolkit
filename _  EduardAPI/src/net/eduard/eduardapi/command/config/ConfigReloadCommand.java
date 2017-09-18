
package net.eduard.eduardapi.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;

public class ConfigReloadCommand extends CMD {

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
			if (API.existsPlugin(sender, cmd)) {
				Plugin pl = API.getPlugin(cmd);
				if (args.length == 2) {
					Config.reloadConfigs(pl);
					API.chat(sender,
							"§aRecarregando todas configurações do Plugin "
									+ pl.getName());
				} else {

				}
			}

		}

		return true;
	}

}
