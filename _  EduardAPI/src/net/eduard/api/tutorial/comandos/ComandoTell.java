
package net.eduard.api.tutorial.comandos;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoTell implements CommandExecutor  {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
		String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			if(args.length <=1) {
				p.sendMessage("/"+label + " <player>");
			}else {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					
				}
			}
 			
		}
		
			
		return true;
	}


}
