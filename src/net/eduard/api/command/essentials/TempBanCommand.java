
package net.eduard.api.command.essentials;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.CommandManager;

public class TempBanCommand extends CommandManager {
	public Map<String, Long> tempbanned = new HashMap<>();
	public Map<String, Long> tempban = new HashMap<>();
	public String message = "§6O jogador §e$target §6foi banido temporariamente por §a$sender";
	public String messageTarget = "§6Voce foi banido pelo §e$target §6por §etempo:$time";
	public String messageNoJoin = "§6Voce foi banido temporariamente!";
	public TempBanCommand() {
		super("tempban");
	}
	@Override
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length <= 1) {
			return false;
		}
		if (Mine.existsPlayer(sender, args[0])) {
			Player target = Mine.getPlayer(args[0]);

			int time = 20;
			int result = time;
			String type = " segundos";
			try {
				time = Integer.valueOf(args[1].replace("s", "").replace("h", "")
						.replace("d", "").replace("m", ""));
			} catch (Exception e) {
			}
			if (args[1].contains("d")) {
				type = " dias";
				result *= 60 * 60 * 24;
			}
			if (args[1].contains("h")) {
				type = " horas";
				result *= 60 * 60;
			}
			if (args[1].contains("m")) {
				type = " minutos";
				result *= 60;
			}
			String text = time + type;
			tempban.put(target.getName(), Mine.getNow());
			tempbanned.put(target.getName(), result * 1000L);
			target.setBanned(true);
			target.kickPlayer(messageTarget.replace("$target", sender.getName())
					.replace("$sender", sender.getName())
					.replace("$time", text));
			Mine.broadcast(message.replace("$target", target.getDisplayName())
					.replace("$sender", sender.getName()));
		}

		return true;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(AsyncPlayerPreLoginEvent e) {
		String name = e.getName();
		OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		if (tempban.containsKey(name)) {
			
			long now = Mine.getNow();
			Long before = tempban.get(name);
			Long dif = tempbanned.get(name);
			if (((dif + before) > now)) {
				e.setKickMessage(messageNoJoin);
				e.setLoginResult(Result.KICK_BANNED);
			} else {
				e.setLoginResult(Result.ALLOWED);
				player.setBanned(false);
			}
			if (!player.isBanned()){
				tempban.remove(name);
				tempbanned.remove(name);
			}
		}

	}
}
