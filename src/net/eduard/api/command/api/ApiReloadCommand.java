
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
			String sub = args[1];
			if (Mine.existsPlugin(sender, sub)) {
				Plugin pl = Mine.getPlugin(sub);
				if (pl instanceof EduardPlugin) {
					EduardPlugin eduardPlugin = (EduardPlugin) pl;
					eduardPlugin.reload();
					sender.sendMessage("§cPlugin do Eduard "+pl.getName()+" foi recarregado");
					
				}else {
					sender.sendMessage("§cEste plugin nao é um plugin do Eduard");
				}
			}
		}

		return true;
	}

}
