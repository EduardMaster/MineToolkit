package net.eduard.api.command;

import java.util.Collection;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.config.ConfigSection;
import net.eduard.api.manager.CMD;

public class WarpsCommand extends CMD {
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
			API.chat(sender,messageEmpty );
		}else {StringBuilder builder = new StringBuilder();
		int v = 0;
		for (ConfigSection warp : warps) {
			if (v != 0) {
				builder.append(", ");
			}
			v++;
			builder.append(warp.getKey());
		}
			API.chat(sender,message.replace("$warps", builder.toString()) );
		}
		return true;
	}

}
