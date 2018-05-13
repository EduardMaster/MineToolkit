
package net.eduard.api.command.essentials.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.SpigotAPI.Chat;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class StaffChatCommand extends CommandManager {
	public StaffChatCommand() {
		super("staffchat");
	}
	public String staffTag = "[STAFF] ";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;

			StringBuilder builder = new StringBuilder();
			int id = 0;
			for (String arg : args) {
				if (id != 0)
					builder.append(" ");
				else
					id++;

				builder.append(Mine.toChatMessage(arg));
			}
			Mine.broadcast(staffTag + p.getDisplayName() + Chat.getArrowRight()
					+ " " + builder.toString(), getCommand().getPermission());
		}
		return true;
	}

}
