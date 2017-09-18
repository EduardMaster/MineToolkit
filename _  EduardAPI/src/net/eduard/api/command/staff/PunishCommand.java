package net.eduard.api.command.staff;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.manager.CMD;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PunishCommand extends CMD {
	public List<String> messages = new ArrayList<>();
	public List<String> commands = new ArrayList<>();
	public List<String> hovers = new ArrayList<>();
	public String header = "Punições para o Jogador $player";
	public PunishCommand() {
		super("punish", "punir");
		messages.add("Banir jogador permanente");
		commands.add("/ban $player");
		hovers.add("Ao clicar ira Banir jogador permanente");

	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player)sender;
			if (args.length <= 1) {
				sendUsage(sender);
			} else {
				
				String player = args[0];
				if (API.existsPlayer(sender, player)) {
					Player target = API.getPlayer(player);
					sender.sendMessage(header.replace("$player", target.getName()));
					int id = 0;
					for (String message : messages) {
						try {
							TextComponent text = new TextComponent(message.replace("$player",
									target.getName()));
							text.setHoverEvent(
									new HoverEvent(HoverEvent.Action.SHOW_TEXT,
											new ComponentBuilder(hovers.get(id).replace("$player",
													target.getName()))
													.create()));
							text.setClickEvent(new ClickEvent(
									ClickEvent.Action.RUN_COMMAND,
									commands.get(id).replace("$player",
											target.getName())));
						
							p.spigot().sendMessage(text);

						} catch (Exception e) {
							e.printStackTrace();
						}

						id++;

					}
				}
			}
		}
		return true;
	}

}
