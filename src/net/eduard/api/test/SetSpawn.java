package net.eduard.api.test;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.EduardAPI;
import net.eduard.api.lib.core.ConfigAPI;

public class SetSpawn implements CommandExecutor {

	public static ConfigAPI config = new ConfigAPI("configuracao.yml",EduardAPI.getInstance());
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (sender instanceof Player) {
			
			Player p = (Player) sender;
			config.set("spawn", p.getLocation());
			config.saveConfig();
			p.sendMessage("§cSpawn setado");
			config.reloadConfig();
			System.out.println(config.getLocation("spawn"));
			
			
		}
		return false;
	}

}
