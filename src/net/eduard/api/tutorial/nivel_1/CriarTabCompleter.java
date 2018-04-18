package net.eduard.api.tutorial.nivel_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CriarTabCompleter implements TabCompleter{

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage("§aVoce esta apertando o Tab");
			if (args.length == 0) {
				return null;
			}else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("usar")) {
					return Arrays.asList("comando","subcomando");
				}
			}else {
				if (args[1].equalsIgnoreCase("comando")) {
					List<String> lista = new ArrayList<>();
					lista.add("outrosubcomando");
					return lista;
				}
			}
			
		}else {
			
		}
		return null;
	}
	

}
