package net.eduard.api.command.essentials;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.click.PlayerEffect;
import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Delay;
import net.eduard.api.setup.game.Sounds;
import net.eduard.api.setup.game.Title;
import net.eduard.api.setup.manager.CommandManager;

public class HomeCommand extends CommandManager {
	public HomeCommand() {
		super("home");
	}
	public Config config = new Config("homes.yml");
	public Sounds sound = Sounds.create("ENDERMAN_TELEPORT");
	public String message = "§6Voce teleportado para sua Home!";
	public String messageError = "§cSua home não foi setada!";
	public Delay delay = new Delay();
	public Title title = new Title(20, 20 * 2, 20, "§6Casa §e$home",
			"§bTeleportado para sua casa §3$home!");
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (Mine.onlyPlayer(sender)) {
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
						Mine.chat(p, message.replace("$home", homex));
						Mine.sendTitle(p,
								title.getTitle().replace("$home", homex),
								title.getSubTitle().replace("$home", homex),
								title.getFadeIn(), title.getStay(), title.getFadeOut());
					
					}
				});
			
			} else {
				Mine.chat(p, messageError.replace("$home", home));
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
