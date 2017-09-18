
package net.eduard.api.command.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ChatAPI;
import net.eduard.api.setup.ExtraAPI;

public class StaffChatCommand extends CMD {
	public StaffChatCommand() {
		super("staffchat");
	}
	public String staffTag = "[STAFF] ";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;

			StringBuilder builder = new StringBuilder();
			int id = 0;
			for (String arg : args) {
				if (id != 0)
					builder.append(" ");
				else
					id++;

				builder.append(ExtraAPI.toChatMessage(arg));
			}
			API.broadcast(staffTag + p.getDisplayName() + ChatAPI.getArrowRight()
					+ " " + builder.toString(), getCommand().getPermission());
		}
		return true;
	}

}
