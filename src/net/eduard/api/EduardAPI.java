package net.eduard.api;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import net.eduard.api.command.EnchantCommand;
import net.eduard.api.command.GotoCommand;
import net.eduard.api.command.SoundCommand;
import net.eduard.api.command.api.ApiCommand;
import net.eduard.api.command.map.MapCommand;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.bungee.BukkitController;
import net.eduard.api.lib.bungee.BungeeAPI;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.event.PlayerTargetEvent;
import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.lib.manager.DBManager;
import net.eduard.api.lib.menu.Menu;
import net.eduard.api.lib.modules.BukkitBungeeAPI;
import net.eduard.api.lib.modules.Copyable.CopyDebug;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.ServerAPI.BukkitControl;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.bukkit_storables.BukkitStorables;
import net.eduard.api.manager.BukkitReplacers;
import net.eduard.api.manager.EssentialsEvents;
import net.eduard.api.manager.InfoGenerator;
import net.eduard.api.manager.MassiveFactionReplacers;
import net.eduard.api.manager.McMMOReplacers;
import net.eduard.api.manager.PluginValor;
import net.eduard.api.server.EduardPlugin;

/**
 * Classe Principal do Plugin EduardAPI
 * 
 * @author Eduard
 * @version 1.2
 * @since 1.0
 * 
 * 
 */
public class EduardAPI extends EduardPlugin {

	private static EduardAPI plugin;
	/**
	 * Som do rosnar do gato
	 */
	@SuppressWarnings("unused")
	private static final SoundEffect ROSNAR = SoundEffect.create("CAT_PURR");

	public static EduardAPI getInstance() {
		return plugin;
	}

	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}

	public void onEnable() {
		plugin = this;
		setFree(true);
		BukkitControl.register(this);
		BukkitBungeeAPI.requestCurrentServer();
		BukkitController bukkit = BungeeAPI.getBukkit();
		bukkit.setPlugin(plugin);
		bukkit.register();
		StorageAPI.setDebug(config.getBoolean("debug-storage"));
		DBManager.setDebug(config.getBoolean("debug-db"));
		Menu.setDebug(config.getBoolean("debug-menu"));
		CopyDebug.setDebug(config.getBoolean("debug-copyable"));
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.game");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.menu");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.manager");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.command");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.server");
		StorageAPI.registerClasses(Mine.class);

		BukkitStorables.load();
		Mine.console("§bEduardAPI §fStorables do Bukkit carregado!");
		Mine.resetScoreboards();
		Mine.console("§bEduardAPI §fScoreboards resetadas!");
		asyncTimer(new Runnable() {

			@Override
			public void run() {

				for (Player p : Mine.getPlayers()) {

					PlayerTargetEvent event = new PlayerTargetEvent(p,
							Mine.getTarget(p, Mine.getPlayerAtRange(p.getLocation(), 100)));
					Mine.callEvent(event);

				}

			}
		}, 20, 20);
		new ApiCommand().register();
		new MapCommand().register();

		new EnchantCommand().register();
		new GotoCommand().register();
		new SoundCommand().register();
		new EssentialsEvents().register(this);
//		Mine.console("§bEduardAPI §fCustom Tag e Scoreboard ativado!");
		InfoGenerator.saveObjects(this);

		Mine.console("§bEduardAPI §fBase ativado!");

		Mine.loadMaps();
		Mine.console("§bEduardAPI §fMapas §acarregados!");

		config.add("sound-teleport", Mine.OPT_SOUND_TELEPORT);
		config.add("sound-error", Mine.OPT_SOUND_ERROR);
		config.add("sound-success", Mine.OPT_SOUND_SUCCESS);
		config.saveConfig();
		Mine.OPT_AUTO_RESPAWN = config.getBoolean("auto-respawn");
		Mine.OPT_NO_JOIN_MESSAGE = config.getBoolean("no-join-message");
		Mine.OPT_NO_QUIT_MESSAGE = config.getBoolean("no-quit-message");
		Mine.OPT_NO_DEATH_MESSAGE = config.getBoolean("no-death-message");
		try {
			Extra.MONEY = new DecimalFormat(config.getString("money-format"),
					DecimalFormatSymbols.getInstance(Locale.forLanguageTag(config.getString("money-format-locale"))));
		} catch (Exception e) {
			Mine.console("§cFormato do dinheiro invalido §f" + config.getString("money-format"));
		}

		Mine.MSG_ON_JOIN = config.message("on-join-message");
		Mine.MSG_ON_QUIT = config.message("on-quit-message");
		Mine.OPT_SOUND_TELEPORT = config.getSound("sound-teleport");
		Mine.OPT_SOUND_ERROR = config.getSound("sound-error");
		Mine.OPT_SOUND_SUCCESS = config.getSound("sound-success");
		if (config.getBoolean("auto-rejoin")) {
			for (Player p : Mine.getPlayers()) {
				Mine.callEvent(new PlayerJoinEvent(p, null));
			}
		}

		PluginValor.register();
		int backupTime = config.getInt("backup-minutes") * 20 * 60;
		if (backupTime <= 0) {
			backupTime = 20 * 60 * 10;
		}
		Mine.console("§bEduardAPI §aBackup Automatico das 'storage.yml' ligado a cada " + (backupTime / (20 * 60))
				+ " minutos.");
		asyncTimer(new Runnable() {

			@Override
			public void run() {
				for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
					if (plugin instanceof EduardPlugin) {
						EduardPlugin eduardPlugin = (EduardPlugin) plugin;
						eduardPlugin.backupStorage();
					}
				}
			}
		}, backupTime, backupTime);

		Mine.console("§bEduardAPI §acarregado!");
		new BukkitReplacers();
		new McMMOReplacers();
		new MassiveFactionReplacers();
	}

	@Override
	public void onDisable() {
		Mine.saveMaps();
		Mine.console("§bEduardAPI §aMapas salvados!");
		Mine.console("§bEduardAPI §cdesativado!");
		BungeeAPI.getController().unregister();
	}

}
