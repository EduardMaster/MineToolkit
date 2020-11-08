
package net.eduard.api.command.map;

import net.eduard.api.EduardAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.server.minigame.GameSchematic;
import net.eduard.api.lib.manager.CommandManager;

public class MapCopyCommand extends CommandManager {

	public MapCopyCommand() {
		super("copy","copiar");
		setDescription("Copia os blocos da posição 1 a posção 2");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			GameSchematic schema = EduardAPI.Companion.getSchematic(p);

			schema.copy(p.getLocation());
			p.sendMessage("§bEduardAPI §aMapa copiado!");
		}
		return true;
	}

}
