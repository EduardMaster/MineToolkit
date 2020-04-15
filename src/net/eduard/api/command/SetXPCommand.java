
package net.eduard.api.command;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.manager.CommandManager;

public class SetXPCommand extends CommandManager {

	public SetXPCommand() {
		super("setexperience");
		 
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				return false;
			} else {
				Integer amount = Extra.toInt(args[0]);
				p.setTotalExperience(0);
				p.setExp(0);
				p.setLevel(0);
				p.giveExp(amount);
				p.sendMessage("§aSua xp foi alterada para: §2"+amount);
				p.sendMessage("§aSeu novo nível é: §2"+p.getLevel());
				p.sendMessage("§aSua barra de XP: §2"+ p.getExp());
				
				
			}
		}
		return true;
	}

}
