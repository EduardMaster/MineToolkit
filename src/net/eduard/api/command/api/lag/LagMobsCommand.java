package net.eduard.api.command.api.lag;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.eduard.api.lib.storage.manager.CommandManager;

public class LagMobsCommand extends CommandManager{

	public LagMobsCommand() {
		super("mobs","killmobs","matarmobs","monstros","animais","animals");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		for (World world : Bukkit.getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				if (entity instanceof Player) {
					continue;
				}
				entity.remove();
			}
		}
		sender.sendMessage("§aLimpando os Mobs");
		return true;
	}
}
