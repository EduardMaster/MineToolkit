package net.eduard.api.command.essentials.warp;

import java.util.Collection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.config.Config;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class WarpsCommand extends CommandManager {
	public WarpsCommand() {
		super("warps");

	}
	public Config config = new Config("warps.yml");
	public String message = "§bWarps disponiveis! $warps";
	public String messageEmpty = "§cNenhum Warp disponivel!";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Collection<ConfigSection> warps = config.getConfig().getValues();
		
		if (warps.isEmpty()) {
			Mine.chat(sender,messageEmpty );
		}else {StringBuilder builder = new StringBuilder();
		int v = 0;
		for (ConfigSection warp : warps) {
			if (v != 0) {
				builder.append(", ");
			}
			v++;
			builder.append(warp.getKey());
		}
			Mine.chat(sender,message.replace("$warps", builder.toString()) );
		}
		return true;
	}

}
