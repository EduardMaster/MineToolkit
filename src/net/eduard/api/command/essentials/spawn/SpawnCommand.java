
package net.eduard.api.command.essentials.spawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.PluginDisableEvent;

import net.eduard.api.config.Config;
import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.click.PlayerEffect;
import net.eduard.api.lib.storage.game.Delay;
import net.eduard.api.lib.storage.game.Sounds;
import net.eduard.api.lib.storage.game.Title;
import net.eduard.api.lib.storage.manager.CommandManager;

public class SpawnCommand extends CommandManager {

	public SpawnCommand() {
		super("spawn");

	}
	public Delay delay = new Delay();
	public String message = "§6Voce foi teleportado para o Spawn!";
	public String messageNot = "§cO Spawn não foi setado!";
	public Sounds sound = Sounds.create("LEVEL_UP");
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

		if (Mine.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (!config.contains("spawn")) {
				Mine.chat(p, messageNot);
			} else {
				delay.effect(p, new PlayerEffect() {

					@Override
					public void effect(Player p) {
						Mine.chat(p, message);
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
		if (config.contains("spawn") && teleportOnRespawn) {
			e.setRespawnLocation(config.getLocation("spawn"));
		}
	}
	@EventHandler
	public void event(PlayerJoinEvent e) {
		if (config.contains("spawn")&&teleportOnJoin) {
			e.getPlayer().teleport(config.getLocation("spawn"));
		}
	}
}
