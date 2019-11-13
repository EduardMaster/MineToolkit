
package net.eduard.api.command.api;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.server.EduardPlugin;

public class ApiSaveCommand extends CommandManager {

	public ApiSaveCommand() {
		super("save", "salvar");
		setUsage("/api save <plugin>");
		setDescription("Salva um plugin feito pelo Eduard");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			sendUsage(sender);
		} else {
			String sub = args[1];
			if (Mine.existsPlugin(sender, sub)) {
				Plugin plugin = Mine.getPlugin(sub);
				if (plugin instanceof EduardPlugin) {
					EduardPlugin eduardPlugin = (EduardPlugin) plugin;
					eduardPlugin.save();
					sender.sendMessage("§bEduardAPI §aPlugin do Eduard " + plugin.getName() + "§a foi salvado");

				} else {
					sender.sendMessage("§bEduardAPI §cEste plugin nao é um plugin do Eduard");
				}
			}
		}

		return true;
	}

}
