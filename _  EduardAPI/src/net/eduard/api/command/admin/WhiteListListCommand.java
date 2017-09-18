
package net.eduard.api.command.admin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class WhiteListListCommand extends CMD {

	public String message = " §6Jogadores na WhiteList: ";
	public String prefix = " §a";
	public WhiteListListCommand() {
		super("list", "jogadores","lista");

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		API.chat(sender, message);
		for (OfflinePlayer player:Bukkit.getWhitelistedPlayers()){
			API.chat(sender,prefix+player.getName());
		}
		return true;
	}
	public static Map<Player, Long> delay = new HashMap<Player, Long>();



}
