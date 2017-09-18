
package net.eduard.eduardapi.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;

public class ConfigSaveAllCommand extends CMD {

	public ConfigSaveAllCommand() {
		super("all","todas");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Config.reloadConfigs();
		API.chat(sender,
				"§aTodas configurações de todos plugins foram salvadas!");
		
		return true;
	}

}
