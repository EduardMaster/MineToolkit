
package net.eduard.api.command.essentials.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class InventorySeeCommand extends CommandManager {
	public String messageTarget = "§6Voce abriu o Inventario do §e$target";
	public InventorySeeCommand() {
		super("inventorysee");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (Mine.existsPlayer(sender, args[0])) {
				Player target = Mine.getPlayer(args[0]);
				p.openInventory(target.getInventory());
				Mine.chat(p,messageTarget.replace("$target", target.getDisplayName()));
			}
		}

		return true;
	}
}
