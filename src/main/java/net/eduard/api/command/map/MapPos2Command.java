
package net.eduard.api.command.map;

import net.eduard.api.EduardAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.minigame.GameSchematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapPos2Command extends CommandManager {

	public MapPos2Command() {
		super("pos2","sethigh","setpos2");
		setDescription("Define a posição 2");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			GameSchematic schema = EduardAPI.Companion.getSchematic(p);
			schema.setHigh(p.getLocation().toVector());
			p.sendMessage("§bEduardAPI §aPosicão 2 setada!");
		}
		return true;
	}

}
