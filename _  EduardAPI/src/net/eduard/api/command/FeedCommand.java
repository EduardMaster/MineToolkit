
package net.eduard.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.manager.CMD;

public class FeedCommand extends CMD {
	
	public FeedCommand() {
		super("feed");
		
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		return true;
	}

}
