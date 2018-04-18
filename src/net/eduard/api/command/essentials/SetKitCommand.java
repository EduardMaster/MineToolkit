
package net.eduard.api.command.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class SetKitCommand extends CommandManager {

	public String message = "§6Seu inventario foi aplicado para todos jogadores!";
	public SetKitCommand() {
		super("setkit");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			int range = 100;
			try {
				Integer.valueOf(args[0]);
			} catch (Exception e) {
			}
			Player p = (Player) sender;
			for (Player player : Mine.getPlayerAtRange(p.getLocation(), range)) {
				if (player != p) {
					player.getInventory().setArmorContents(
							p.getInventory().getArmorContents());
					player.getInventory()
							.setContents(p.getInventory().getContents());

				}
			}
			Mine.chat(p, message);
		}

		return true;
	}

}
