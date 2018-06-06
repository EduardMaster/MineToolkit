
package net.eduard.api.command.config;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.lib.manager.CommandManager;

public class ConfigHelpCommand extends CommandManager {

	public ConfigHelpCommand() {
		super("help", "ajuda");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		sender.sendMessage("§6§l                  AJUDA");
		sender.sendMessage(" ");
		sender.sendMessage(
				"§b/" + label + " reload [plugin|all] [config-name]");
		sender.sendMessage("  §aRecarrega a configuração dos Plugins");
		sender.sendMessage("§b/" + label + " save [plugin|all] [config-name]");
		sender.sendMessage("  §aSalva a configuração dos Plugins");
		return true;
	}

}
