package net.eduard.api.command;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.game.Delay;
import net.eduard.api.game.PlayerEffect;
import net.eduard.api.game.Title;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ExtraAPI;
import net.eduard.api.setup.RexAPI;

public class WarpCommand extends CMD {
	public WarpCommand() {
		super("warp");
	}
	public Config config = new Config("warps.yml");
	public Delay delay = new Delay();
	public String message = "§6Voce teleportado ate o $warp";
	public String messageError = "§cNão existe este Warp";
	public Title title = new Title(20, 20 * 2, 20, "§6Warp §e$warp",
			"§2Você foi para a warp §a$warp!");
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			} else {
				String warp = args[0];
				String path = warp.toLowerCase();
				if (config.contains(path)) {
					if (API.hasPerm(p, getPermission() + "." + warp)) {
						delay.effect(p, new PlayerEffect() {

							@Override
							public void effect(Player p) {
								p.teleport(config.getLocation(path));
								API.SOUND_TELEPORT.create(p);
								API.chat(p, message.replace("$warp", warp));
								RexAPI.sendTitle(p,
										title.getTitle().replace("$warp", warp),
										title.getSubTitle().replace("$warp",
												warp),
										title.getFadeIn(), title.getStay(),
										title.getFadeOut());
							}
						});
					}
				} else {
					API.chat(p, messageError.replace("$warp", warp));
					config.remove(path);
				}
			}

		}

		return true;
	}
	@EventHandler
	public void event(PlayerCommandPreprocessEvent e) {
		String msg = e.getMessage();
		Set<String> warps = config.getKeys();
		for (String warp : warps) {
			if (ExtraAPI.commandEquals(msg, "/" + warp)) {
				e.setMessage("/warp " + warp);
				break;
			}
		}
	}
	@EventHandler
	public void event(PluginDisableEvent e) {
		if (e.getPlugin().equals(getPlugin())) {
			config.saveConfig();
		}
	}

}
