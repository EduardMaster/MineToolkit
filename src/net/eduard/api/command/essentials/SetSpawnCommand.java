package net.eduard.api.command.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class SetSpawnCommand extends CommandManager {

	public SetSpawnCommand() {
		super("setspawn");
	}
	public Config config = new Config("spawn.yml");
	public String message = "§bVoce setou o Spawn!";
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			config.set("spawn", p.getLocation());
			config.saveConfig();
			Mine.chat(p, message);
		}

		return true;
	}
	

}
