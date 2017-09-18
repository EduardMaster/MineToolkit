
package net.eduard.api.command;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.game.Delay;
import net.eduard.api.game.PlayerEffect;
import net.eduard.api.game.Sounds;
import net.eduard.api.game.Title;
import net.eduard.api.manager.CMD;

public class SpawnCommand extends CMD {

	public SpawnCommand() {
		super("spawn");

	}
	public Delay delay = new Delay();
	public String message = "§6Voce foi teleportado para o Spawn!";
	public String messageNot = "§cO Spawn não foi setado!";
	public Sounds sound = Sounds.create(Sound.LEVEL_UP);
	public Config config = new Config("spawn.yml");
	public Title title = new Title(20, 20 * 2, 20, "§6Inicio",
			"§eVoce foi para o Spawn!");
	public boolean teleportOnRespawn = true;
	public boolean teleportOnJoin = true;
	@EventHandler
	public void event (PluginDisableEvent e) {
		if (e.getPlugin().equals(getPlugin())) {
			config.saveConfig();
		}
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!config.contains("spawn")) {
				API.chat(p, messageNot);
			} else {
				delay.effect(p, new PlayerEffect() {

					@Override
					public void effect(Player p) {
						API.chat(p, message);
						p.teleport(config.getLocation("spawn"));
						sound.create(p);
						title.create(p);
					}
				});

			}

		}

		return true;
	}

	@EventHandler
	public void event(PlayerRespawnEvent e) {
		if (config.contains("spawn") & teleportOnRespawn) {
			e.setRespawnLocation(config.getLocation("spawn"));
		}
	}
	@EventHandler
	public void event(PlayerJoinEvent e) {
		if (config.contains("spawn")&teleportOnJoin) {
			e.getPlayer().teleport(config.getLocation("spawn"));
		}
	}
}
