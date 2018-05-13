
package net.eduard.api.command.essentials.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.CommandManager;

public class TeleportAllCommand extends CommandManager {
	public TeleportAllCommand() {
		super("teleportall");
	}
	public String message = "§6Voce teleportou todos ate você!";
	public String messageTarget = "§6Voce foi teleportado pelo jogador $player§6!";
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			for (Player d : Mine.getPlayers()) {
				if (d.equals(p))
					continue;
				d.teleport(p);
				Mine.chat(d,
						messageTarget.replace("$player", p.getDisplayName()));
			}
			Mine.chat(p,message);
		}
		return true;
	}

}
