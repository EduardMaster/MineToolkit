package net.eduard.api.tutorial.nivel_1;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ComandoChatLimpo implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("clearchat")) {
			for (int i = 0; i < 30; i++) {
				Bukkit.broadcastMessage("");
			}
			Bukkit.broadcastMessage("§a                   Chat Limpo");
		}
		return false;
	}

	
}
