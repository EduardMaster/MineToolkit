
package net.eduard.api.command.staff;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;

public class MuteCommand extends CMD {

	public String messageTarget = "§6O jogador §e$target §6foi mutado por §a$sender §6motido: §c$reason";
	public String message = "§cVoce foi mutado";
	public MuteCommand() {
		super("mute");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length <= 1) {
			return false;
		}
		if (API.existsPlayer(sender, args[0])) {
			Player target = API.getPlayer(args[0]);
			StringBuilder builder = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				builder.append(args[i] + " ");
			}
			broadcast(messageTarget.replace("$target", target.getDisplayName())
					.replace("$sender", sender.getName())
					.replace("$reason", builder.toString()));
			API.chat(target,message);
			target.setMetadata("muted",
					new FixedMetadataValue(getPlugin(), true));
		}
		return true;
	}
	@EventHandler
	public void event(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (p.hasMetadata("muted")) {
			e.setCancelled(true);
			API.chat(p,message);

		}
	}
}
