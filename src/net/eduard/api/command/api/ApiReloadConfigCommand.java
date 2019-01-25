
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.CommandManager;

public class ApiReloadConfigCommand extends CommandManager {

	public ApiReloadConfigCommand() {
		super("reloadconfig", "recarregarconfig");
		setUsage("/api reloadconfig <plugin>|all");
		setDescription("Recarrega as configs de um plugin feito pelo Eduard");

	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String sub = args[1];
			if (sub.equalsIgnoreCase("all")) {
			
				for (Config config : Config.CONFIGS) {
					config.reloadConfig();
				}
				sender.sendMessage("§aTodas configuracoes de todos plugins foram recarregadas!");
			} else {
				if (Mine.existsPlugin(sender, sub)) {
					Plugin pl = Mine.getPlugin(sub);
					for (Config config : Config.CONFIGS) {
						if (config.getPlugin().equals(pl)) {
							config.reloadConfig();
						}

					}
					sender.sendMessage("§aRecarregando todas configurações do Plugin " + pl.getName());

				}
			}

		}

		return true;
	}

}
