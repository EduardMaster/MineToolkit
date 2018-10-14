
package net.eduard.api.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.config.Config;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class ConfigSaveAllCommand extends CommandManager {

	public ConfigSaveAllCommand() {
		super("all","todas");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Config.reloadConfigs();
		Mine.chat(sender,
				"�aTodas configura��es de todos plugins foram salvadas!");
		
		return true;
	}

}
