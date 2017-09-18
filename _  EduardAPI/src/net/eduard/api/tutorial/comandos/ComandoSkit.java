
package net.eduard.api.tutorial.comandos;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoSkit implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
		String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			for (Player player:Bukkit.getOnlinePlayers()) {
				if (player!=p) {
						player.getInventory().setArmorContents(p.getInventory().getArmorContents());
						player.getInventory().setContents(p.getInventory().getContents());
					
				}
			}
			p.sendMessage("§6Seu inventario foi aplicado para todos jogadores!");
		}
		return true;
	}

}
