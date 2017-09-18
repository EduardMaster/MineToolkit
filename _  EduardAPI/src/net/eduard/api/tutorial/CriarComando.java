package net.eduard.api.tutorial;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CriarComando implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage("§c/comando usar");
			}else {
				if (args[0].equalsIgnoreCase("usar")) {
					player.sendMessage("§aEnvia uma mensagem Simples!");
				}
			}
			
		}else {
			sender.sendMessage("§cComando apenas para jogadores!");
		}
		return false;
	}

}
