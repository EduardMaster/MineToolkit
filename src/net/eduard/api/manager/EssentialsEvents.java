package net.eduard.api.manager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

import net.eduard.api.EduardAPI;
import net.eduard.api.config.Config;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.game.Schematic;
import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.server.EduardPlugin;

public class EssentialsEvents extends EventsManager {
	@EventHandler
	public void onEnable(PluginEnableEvent e) {
		if (e.getPlugin() instanceof EduardPlugin) {

			EduardPlugin plugin = (EduardPlugin) e.getPlugin();
			String msg = "§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().getVersion()
					+ "§a foi ativado com sucesso.";
			if (plugin.isFree()) {
				Mine.broadcast(msg);
			} else {

				Mine.console(msg);
			}
		}
		for (Config config : Config.CONFIGS) {
			if (e.getPlugin().equals(EduardAPI.getInstance().getConfigs().getPlugin())) {
				config.reloadConfig();
			}
		}
	}

	@EventHandler
	public void marketing(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (p.hasPermission("eduard.plugins")) {
			for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
				if (plugin instanceof EduardPlugin) {
					if (plugin.isEnabled()) {
						p.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv"
								+ plugin.getDescription().getVersion() + "§a esta ativado.");
					} else {
						p.sendMessage("§b[Eduard-Dev] §f" + plugin.getName() + " §fv"
								+ plugin.getDescription().getVersion() + "§c esta desativado.");
					}

				}
			}
			p.sendMessage("§aCaso deseje comprar mais plugins entre em contato ou no site §bwww.eduarddev.tk");
		}

	}

	@EventHandler
	public void onDisable(PluginDisableEvent e) {
		if (e.getPlugin() instanceof EduardPlugin) {

			EduardPlugin plugin = (EduardPlugin) e.getPlugin();
			String msg = "§b[Eduard-Dev] §f" + plugin.getName() + " §fv" + plugin.getDescription().getVersion()
					+ " foi desativado com sucesso.";
			if (plugin.isFree()) {
				Mine.broadcast(msg);
			} else {
				Mine.console(msg);
			}

		}
		for (Config config : Config.CONFIGS) {
			if (e.getPlugin().equals(EduardAPI.getInstance().getConfigs().getPlugin())) {
				if (config.isAutoSave()) {
					config.saveConfig();
				}
			}
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (Mine.OPT_AUTO_RESPAWN) {
			if (p.hasPermission("eduardAPI.autorespawn")) {
				Mine.TIME.delay(1L, new Runnable() {

					@Override
					public void run() {
						if (p.isDead()) {
							p.setFireTicks(0);
							try {
								Mine.makeRespawn(p);
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
			}

		}
		if (Mine.OPT_NO_DEATH_MESSAGE) {
			e.setDeathMessage(null);
		}
	}

	@EventHandler
	public void onPingServer(ServerListPingEvent e) {
		Integer amount = EduardAPI.getInstance().getConfigs().getInt("custom-motd-amount");
		if (amount > -1) {
			e.setMaxPlayers(amount);
		}

		if (EduardAPI.getInstance().getConfigs().getBoolean("custom-motd")) {
			StringBuilder builder = new StringBuilder();
			for (String line : EduardAPI.getInstance().getConfigs().getMessages("motd")) {
				builder.append(line + "\n");
			}
			e.setMotd(builder.toString());
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (EduardAPI.getInstance().getConfigs().getBoolean("custom-quit-message"))
			e.setQuitMessage(Mine.MSG_ON_QUIT.replace("$player", p.getName()));
		if (Mine.OPT_NO_QUIT_MESSAGE) {
			e.setQuitMessage("");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (EduardAPI.getInstance().getConfigs().getBoolean("save-players")) {
			InfoGenerator.saveObject("Players/" + p.getName() + " " + p.getUniqueId(), p);
		}
		if (EduardAPI.getInstance().getConfigs().getBoolean("custom-join-message")) {
			e.setJoinMessage(Mine.MSG_ON_JOIN.replace("$player", p.getName()));
		}

		if (Mine.OPT_NO_JOIN_MESSAGE) {
			e.setJoinMessage(null);
			return;
		}

	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			if (e.getItem() == null)
				return;
			if (e.getItem().getType() == Material.WOOD_AXE) {
				Schematic mapa = Mine.getSchematic(p);
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					mapa.setHigh(e.getClickedBlock().getLocation().toVector());
					p.sendMessage("§bEduardAPI §6Posi§§o 1 setada!");
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					mapa.setLow(e.getClickedBlock().getLocation().toVector());
					p.sendMessage("§bEduardAPI §6Posi§§o 2 setada!");
				}

			}
		}
	}
}
