package net.eduard.api.command.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;

public class SetSpawnCommand extends CMD {

	public SetSpawnCommand() {
		super("setspawn");
	}
	public Config config = new Config("spawn.yml");
	public String message = "§bVoce setou o Spawn!";
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			config.set("spawn", p.getLocation());
			config.saveConfig();
			API.chat(p, message);
		}

		return true;
	}
	

}
