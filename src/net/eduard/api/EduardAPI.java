package net.eduard.api;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.command.EnchantCommand;
import net.eduard.api.command.GotoCommand;
import net.eduard.api.command.SoundCommand;
import net.eduard.api.command.api.ApiCommand;
import net.eduard.api.command.config.ConfigCommand;
import net.eduard.api.command.map.MapCommand;
import net.eduard.api.lib.EduardLIB;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.bungee.BukkitController;
import net.eduard.api.lib.bungee.BungeeAPI;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.DBManager;
import net.eduard.api.lib.manager.DropManager;
import net.eduard.api.lib.manager.PlayersManager;
import net.eduard.api.lib.manager.TimeManager;
import net.eduard.api.lib.menu.Menu;
import net.eduard.api.lib.menu.Shop;
import net.eduard.api.lib.modules.BukkitBungeeAPI;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.bukkit_storables.BukkitStorables;
import net.eduard.api.manager.BukkitReplacers;
import net.eduard.api.manager.EssentialsEvents;
import net.eduard.api.manager.InfoGenerator;
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

	public static EduardAPI getInstance() {
		return plugin;
	}

	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}

	public void onLoad() {

	}

	public void onEnable() {
		plugin = this;
		setFree(true);
		// BukkitControl.register(this);
		// BukkitAPI.register(this);
		config = new Config(this, "config.yml");
		messages = new Config(this, "messages.yml");
		BukkitBungeeAPI.requestCurrentServer();
		BukkitController bukkit = BungeeAPI.getBukkit();
		bukkit.setPlugin(plugin);
		bukkit.register();
//		StorageAPI.set(config.getBoolean("debug-storage"));
		DBManager.setDebug(config.getBoolean("debug-db"));

		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.game");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.menu");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib.manager");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.command");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.server");
		StorageAPI.registerClasses(Mine.class);

		BukkitStorables.load();
		Mine.resetScoreboards();
		Mine.console("§bEduardAPI §fScoreboards resetadas!");
		BukkitReplacers.registerRplacers();
		asyncTimer(new Runnable() {

			@Override
			public void run() {
				Mine.updateTargets();
			}
		}, 20, 20);
		new ApiCommand().register();
		new MapCommand().register();
		new ConfigCommand().register();
		new EnchantCommand().register();
		new GotoCommand().register();
		new SoundCommand().register();
		new EssentialsEvents().register(this);
		Mine.console("§bEduardAPI §fCustom Tag e Scoreboard ativado!");
		InfoGenerator.saveObjects(this);

		new DropManager().register(this);
		Mine.console("§bEduardAPI §fCustom drops ativado!");
//		Mine.registerEvents(this, this);
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

		Mine.setPlayerManager(new PlayersManager());
		Mine.getPlayerManager().register(this);
		Mine.console("§bEduardAPI §acarregado!");
		Mine.console("§cTestando " + StorageAPI.getStore(Shop.class));
		Mine.console("§cTestando " + StorageAPI.getStore(Menu.class));
//		Mine.console("§cPreco EduardAPI: "+Mine.calculatePluginValue(this, "net.eduard.api"));
	}

	@Override
	public void onDisable() {
		Mine.saveMaps();
		Mine.console("§bEduardAPI §aMapas salvados!");
		Mine.console("§bEduardAPI §cdesativado!");
		BungeeAPI.getController().unregister();
	}



}
