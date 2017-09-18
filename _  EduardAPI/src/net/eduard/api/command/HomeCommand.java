package net.eduard.api.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.game.Delay;
import net.eduard.api.game.PlayerEffect;
import net.eduard.api.game.Sounds;
import net.eduard.api.game.Title;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.RexAPI;

public class HomeCommand extends CMD {
	public HomeCommand() {
		super("home");
	}
	public Config config = new Config("homes.yml");
	public Sounds sound = Sounds.create(Sound.ENDERMAN_TELEPORT);
	public String message = "§6Voce teleportado para sua Home!";
	public String messageError = "§cSua home não foi setada!";
	public Delay delay = new Delay();
	public Title title = new Title(20, 20 * 2, 20, "§6Casa §e$home",
			"§bTeleportado para sua casa §3$home!");
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			String home = "home";
			if (args.length >= 1) {
				home = args[0];
			}
			String path = p.getUniqueId().toString() + "." + home;
			if (config.contains(path)) {
				final String homex = home;
				delay.effect(p, new PlayerEffect() {
					
					@Override
					public void effect(Player p) {
						p.teleport(config.getLocation(path));
						sound.create(p);
						API.chat(p, message.replace("$home", homex));
						RexAPI.sendTitle(p,
								title.getTitle().replace("$home", homex),
								title.getSubTitle().replace("$home", homex),
								title.getFadeIn(), title.getStay(), title.getFadeOut());
					
					}
				});
			
			} else {
				API.chat(p, messageError.replace("$home", home));
				config.remove(path);
			}

		}

		return true;
	}
	@EventHandler
	public void event(PluginDisableEvent e) {
		if (e.getPlugin().equals(getPlugin())) {
			config.saveConfig();
		}
	}

}
