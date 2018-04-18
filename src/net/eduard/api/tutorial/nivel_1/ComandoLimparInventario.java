package net.eduard.api.tutorial.nivel_1;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoLimparInventario implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player jogador = (Player) sender;
			if (command.getName().equalsIgnoreCase("clearinventory")) {
				jogador.getInventory().clear();
				jogador.getInventory().setArmorContents(null);
				jogador.sendMessage("§aInventario Limpo!");
			}
		}
		
		return false;
	}

}
