package net.eduard.api.command.essentials;

import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.click.PlayerEffect;
import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Delay;
import net.eduard.api.setup.game.Title;
import net.eduard.api.setup.manager.CommandManager;

public class WarpCommand extends CommandManager {
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
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (args.length == 0) {
				sendUsage(sender);
			} else {
				String warp = args[0];
				String path = warp.toLowerCase();
				if (config.contains(path)) {
					if (Mine.hasPerm(p, getPermission() + "." + warp)) {
						delay.effect(p, new PlayerEffect() {

							@Override
							public void effect(Player p) {
								p.teleport(config.getLocation(path));
								Mine.SOUND_TELEPORT.create(p);
								Mine.chat(p, message.replace("$warp", warp));
								Mine.sendTitle(p,
										title.getTitle().replace("$warp", warp),
										title.getSubTitle().replace("$warp",
												warp),
										title.getFadeIn(), title.getStay(),
										title.getFadeOut());
							}
						});
					}
				} else {
					Mine.chat(p, messageError.replace("$warp", warp));
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
			if (Mine.startWith(msg, "/" + warp)) {
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
