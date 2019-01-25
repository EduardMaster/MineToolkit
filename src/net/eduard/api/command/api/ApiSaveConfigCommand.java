
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.CommandManager;

public class ApiSaveConfigCommand extends CommandManager {

	public ApiSaveConfigCommand() {
		super("saveconfig", "salvarconfig");
		setUsage("/api saveconfig <plugin>|all");
		setDescription("Salva as configs de um plugin feito pelo Eduard");
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String sub = args[1];
			if (sub.equalsIgnoreCase("all")) {
				for (Config config : Config.CONFIGS) {
					config.saveConfig();
				}
				sender.sendMessage(
						"§aTodas configurações de todos plugins foram salvadas!");
			}else {
				if (Mine.existsPlugin(sender, sub)) {
					Plugin pl = Mine.getPlugin(sub);
					for (Config config : Config.CONFIGS) {
						if (config.getPlugin().equals(pl)) {
							config.saveConfig();
						}

					}
					sender.sendMessage("§aSalvandos todas configurações do Plugin " + pl.getName());
				}
			}
		

		}

		return true;
	}

}
