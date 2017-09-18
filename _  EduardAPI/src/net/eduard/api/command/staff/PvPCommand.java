
package net.eduard.api.command.staff;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class PvPCommand extends CMD {

	public String messageOn = "§6O PvP foi ativado!";
	public String messageOff = "§6O PvP foi desativado!";
	public PvPCommand() {
		super("pvp");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length ==0 ){
			if (sender instanceof Player){
				Player p = (Player) sender;
				if (p.getWorld().getPVP()){
					p.getWorld().setPVP(false);
				}else{
					p.getWorld().setPVP(true);
				}
				
			}else return false;
		}else{
			String name = args[0];
			if (API.existsWorld(sender, name)){
				World world = Bukkit.getWorld(name);
				if (world.getPVP()) {
					world.setPVP(false);
					API.chat(sender,messageOff);
				} else {
					world.setPVP(true);
					API.chat(sender,messageOn);
				}
			}
		}
		
		return true;
	}

}
